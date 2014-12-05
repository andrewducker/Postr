package com.postr.DataTypes.Outputs;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.DateTool;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;
import com.postr.DAO;
import com.postr.Result;
import com.postr.DataTypes.LinkEntry;
import com.postr.DataTypes.LinkSet;
import com.postr.DataTypes.Inputs.BaseInput;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

@Subclass(index=true)
public abstract class BaseFeed extends BasePost {
	public List<Long> inputs;
	public int daysToInclude = 1;
	@Index public boolean active = true;
	String defaultTags;
	boolean postWithTags;

	abstract BasePost generatePost(String postSubject, String postContents, List<String> tags) throws Exception;
	
	@Override
	public ResultStateEnum PostOrSave() throws Exception{
		if (active && postingTime.isBefore(DateTime.now())) {
			MakePost();
			postingTime = postingTime.plusDays(daysToInclude);
			result.postingTime = DateTime.now(DateTimeZone.UTC).getMillis();
			DAO.SaveThing(this, getParent());
			return ResultStateEnum.posted;
		}
		return ResultStateEnum.ignored;
	}

	
	
	@Override
	void MakePost() throws Exception {
		LinkSet links = getLinks();
		links = FilterLinksByDate(links, postingTime, postingTime.minusDays(daysToInclude));
		if (links.size() == 0) {
			result = Result.Success("No entries found");
		} else {
			result = postLinks(links);
		}
	}

	private Result postLinks(LinkSet links) throws Exception {
		String postSubject = FormatTitle(postingTime, subject);
		String postContents = Format(links, contents);
		List<String> tags = getTags(links);
		long postKey = WritePost(postSubject, postContents, tags);
		return Result.Success("Post " + postKey +  " created at "+postingTime.toString());
	}

	private long WritePost(String postSubject, String postContents,
			List<String> tags) throws Exception {
		BasePost post = generatePost(postSubject, postContents, tags);
		Key<BasePost> postKey = DAO.SaveThing(post);
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(withUrl("/postsinglefeed").param("key", postKey.toString()));			
		return postKey.getId();
	}

	private List<String> getTags(LinkSet links) {
		List<String> tagsForPosting;
		if (postWithTags) {
			tagsForPosting = links.getTagNames();
			String[] extraTags = defaultTags.split(",");
			for (int i = 0; i < extraTags.length; i++) {
				tagsForPosting.add(extraTags[i]);
			}
		}
		else{
			tagsForPosting = new ArrayList<String>();
		}
		
		return tagsForPosting;
		
	}

	private LinkSet getLinks() throws Exception {
		ArrayList<ListenableFuture<LinkSet>> futureLinks = new ArrayList<ListenableFuture<LinkSet>>(); 
		for (Long inputID : inputs) {
			BaseInput input = DAO.LoadThing(BaseInput.class,inputID);
			futureLinks.add(input.Read());
		}
		ListenableFuture<List<LinkSet>> linksFuture = Futures.successfulAsList(futureLinks);
		List<LinkSet> links = linksFuture.get();
		
		LinkSet masterLinkSet = new LinkSet(); 
		
		for (LinkSet linkSet : links) {
			//Combine them together for now.
			masterLinkSet.addAll(linkSet);
		}
		return masterLinkSet;
	}
	
	private static LinkSet FilterLinksByDate(LinkSet links, DateTime startTime, DateTime endTime) {
		LinkSet filteredList = new LinkSet();
		for (LinkEntry linkEntry : links) {
			if (linkEntry.PostedDate.isBefore(endTime)
					&& linkEntry.PostedDate.isAfter(startTime)) {
				filteredList.add(linkEntry);
			}
		}
		return filteredList;
	}
	
	private static String Format(List<LinkEntry> links, String postTemplate) throws Exception
	{
		Velocity.init();
		VelocityContext context = new VelocityContext();
		
		context.put("links",links);
		StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "mystring",postTemplate);
		return writer.toString();
	}
	
	private static String FormatTitle(DateTime endTime, String subjectTemplate) throws Exception
	{
		VelocityContext context = new VelocityContext();
		StringWriter writer = new StringWriter();
		DateTool dateTool = new DateTool();
		context.put("date", dateTool);
		context.put("postingTime",endTime);
		Velocity.evaluate(context, writer, "", subjectTemplate);
		return writer.toString();
	}
}