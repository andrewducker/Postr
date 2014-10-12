package com.postr;

public class PinboardFeedParser extends BaseFeedParser {

	public PinboardFeedParser(String userName) {
		super(userName);
		splitTags = true;
	}

	@Override
	String GetURLForTag(String tag) {
		return "http://pinboard.in/u:"+userName+"/t:"+tag;
		}

}
