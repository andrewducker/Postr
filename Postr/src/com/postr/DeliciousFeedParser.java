package com.postr;

public class DeliciousFeedParser extends BaseFeedParser {

	public DeliciousFeedParser(String userName) {
		super(userName);
		splitTags = false;
	}

	@Override
	String GetURLForTag(String tag) {
		return "http://del.icio.us/"+userName+"/"+tag;
	}
}
