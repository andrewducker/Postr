package com.postr.DataTypes.Inputs;

import org.joda.time.DateTime;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.googlecode.objectify.annotation.Subclass;
import com.postr.BaseFeedParser;
import com.postr.DataTypes.LinkEntry;
import com.postr.DataTypes.LinkSet;
import com.postr.DataTypes.LinkTag;

@SuppressWarnings("serial")
@Subclass(index=true)
public class TestInputData extends BaseInput {

	protected TestInputData(){}
	
	public TestInputData(String userName){
		super(userName);
	}

	
	@Override
	public String getSiteName() {
		return "TestInput";
	}

	@Override
	BaseFeedParser GetParser() {
		return null;
	}

	@Override
	String GetFeedURL() {
		return null;
	}
	
	@Override
	public ListenableFuture<LinkSet> Read() throws Exception {

		SettableFuture<LinkSet> linkSetFuture = SettableFuture.create();
		LinkSet linkSet = new LinkSet();
		LinkEntry linkEntry = new LinkEntry();
		linkEntry.PostedDate = DateTime.now();
		linkEntry.Description = "This is the description for today's test input for " + userName;
		linkEntry.Title = "Test " + userName + " 1";
		linkEntry.URL = "http://test.com/testInput1";
		LinkTag linkTag = new LinkTag();
		linkTag.Tag = "Test1";
		linkTag.TagURL = "http://test.com/test1";
		linkEntry.Tags.add(linkTag);
		linkSet.add(linkEntry);
		
		linkEntry = new LinkEntry();
		linkEntry.PostedDate = DateTime.now().minusDays(1);
		linkEntry.Description = "This is the description for yesterday's test input for " + userName;
		linkEntry.Title = "Test " + userName + " 2";
		linkEntry.URL = "http://test.com/testInput2";
		
		linkTag = new LinkTag();
		linkTag.Tag = "Test1";
		linkTag.TagURL = "http://test.com/test2";
		linkEntry.Tags.add(linkTag);
		linkSet.add(linkEntry);
		
		linkSetFuture.set(linkSet);
		
		return linkSetFuture;
	}


}
