package com.postr;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.joda.time.DateTime;

import uk.org.catnip.eddie.Category;
import uk.org.catnip.eddie.Detail;
import uk.org.catnip.eddie.Entry;
import uk.org.catnip.eddie.FeedData;
import uk.org.catnip.eddie.Link;
import uk.org.catnip.eddie.parser.Parser;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.common.base.Function;
import com.postr.DataTypes.LinkEntry;
import com.postr.DataTypes.LinkSet;
import com.postr.DataTypes.LinkTag;

public abstract class BaseFeedParser implements Function<HTTPResponse, LinkSet> {

	abstract String GetURLForTag(String tag);
	protected Boolean splitTags;
	protected String userName;
	public BaseFeedParser(String userName)
	{
		this.userName = userName;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LinkSet apply(HTTPResponse response) {
		Parser parser = new Parser();
		FeedData feedData = null;
		feedData = parser.parse(response.getContent());
		
		LinkSet links = new LinkSet();
		
		for(Iterator<Entry> entryIterator = feedData.entries();entryIterator.hasNext();)
		{
			Entry entry = entryIterator.next();

			LinkEntry linkEntry = new LinkEntry();
			linkEntry.Title = entry.getTitle().getValue();
			
			Detail commentDetail = entry.getSummary();
			
			if(commentDetail != null)
			{
				if (!commentDetail.getValue().trim().equals("<p></p>") && !commentDetail.getValue().trim().equals("")) {
					 linkEntry.Description = commentDetail.getValue();
				}
			}
			
			linkEntry.PostedDate = new DateTime(entry.getModified());			

			for(Iterator<Link> linkIterator = entry.links();linkIterator.hasNext();)
			{
				 linkEntry.URL = linkIterator.next().getValue();
			}
			
			linkEntry.Tags = GetTagsFromCategories(entry.categories());
			
			links.add(linkEntry);
		}
		return links;
	}


private List<LinkTag> GetTagsFromCategories(Iterator<Category> categoryIterator) {
	List<LinkTag> Tags = new Vector<LinkTag>();
	while(categoryIterator.hasNext())
	{
		String tagText = categoryIterator.next().getTerm();
		String[] tags;
		if (splitTags) {
			tags = tagText.split(" ");
		}
		else {
			tags = new String[1];
			tags[0] = tagText;
		}
		for (String tag : tags) {
			LinkTag linkTag = new LinkTag();
			linkTag.Tag = tag;
			linkTag.TagURL = GetURLForTag(tag);
			Tags.add(linkTag);
		} 
	}
	return Tags;
}
}
