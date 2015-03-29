package com.postr;

public class PinterestFeedParser extends BaseFeedParser {

	public PinterestFeedParser(String userName) {
		super(userName);
		splitTags = false;
	}

	@Override
	String GetURLForTag(String tag) {
		//No tags on Pinterest :-(
		return "";
	}
}
