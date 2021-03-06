package com.postr.DataTypes.Outputs;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.DateTool;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;
import com.postr.DAO;
import com.postr.Result;
import com.postr.DataTypes.LinkEntry;
import com.postr.DataTypes.LinkSet;
import com.postr.DataTypes.Inputs.BaseInput;

@SuppressWarnings("serial")
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
		if (links.size() == 1 && links.get(0).Title.equalsIgnoreCase("something went wrong")) {
			throw new Exception ("Delicious done messed up.  Crash out here");
		}
		links = FilterLinksByDate(links, postingTime.minusDays(daysToInclude), postingTime);
		if (links.size() == 0) {
			result = Result.Success("No entries found");
		} else {
			result = postLinks(links);
		}
	}

	private Result postLinks(LinkSet links) throws Exception {
		Collections.sort(links);
		String postSubject = FormatTitle(postingTime, subject);
		String postContents = Format(links, contents);
		//Possible that there will be no content left once formatting has completed.
		if (postContents.trim().length() == 0) {
			return Result.Success("No entries found");
		}
		List<String> tags = getTags(links);
		long postKey = WritePost(postSubject, postContents, tags);
		return Result.Success("Post " + postKey +  " created at "+postingTime.toString());
	}

	private long WritePost(String postSubject, String postContents,
			List<String> tags) throws Exception {
		BasePost post = generatePost(postSubject, postContents, tags);
		post.postingTime = postingTime;
		Key<BasePost> postKey = DAO.SaveThing(post, this.getParent());
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(withUrl("/MakeSinglePost").param("key", String.valueOf(postKey.getId())));			
		return postKey.getId();
	}

	private List<String> getTags(LinkSet links) {
		List<String> tagsForPosting;
		tagsForPosting = new ArrayList<String>();
		if(defaultTags != null){
			String[] extraTags = defaultTags.split(",");
			for (int i = 0; i < extraTags.length; i++) {
				tagsForPosting.add(extraTags[i]);
			}
		}
		if (postWithTags) {
			tagsForPosting.addAll(links.getTagNames());
		}
		return tagsForPosting;
	}

	private LinkSet getLinks() throws Exception {
		ArrayList<LinkSet> futureLinks = new ArrayList<LinkSet>(); 
		for (Long inputID : inputs) {
			BaseInput input = DAO.LoadThing(BaseInput.class,inputID);
			futureLinks.add(input.Read());
		}

		LinkSet masterLinkSet = new LinkSet(); 

		for (LinkSet links : futureLinks) {
			masterLinkSet.addAll(links);
		}
		
		return masterLinkSet;
	}
	
	@SuppressWarnings("unused")
	//Switching to the Sync version, to see if it solves the problem with missing data.
	private LinkSet getLinksAsync() throws Exception {
		ArrayList<Future<LinkSet>> futureLinks = new ArrayList<Future<LinkSet>>(); 
		for (Long inputID : inputs) {
			BaseInput input = DAO.LoadThing(BaseInput.class,inputID);
			futureLinks.add(input.ReadAsync());
		}

		LinkSet masterLinkSet = new LinkSet(); 

		for (Future<LinkSet> future : futureLinks) {
			masterLinkSet.addAll(future.get());
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
		context.put("postingTime",endTime.toGregorianCalendar());
		Velocity.evaluate(context, writer, "", subjectTemplate);
		return writer.toString();
	}
}
