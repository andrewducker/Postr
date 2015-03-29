package com.postr.DataTypes.Inputs;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.BaseFeedParser;
import com.postr.PinterestFeedParser;

@SuppressWarnings("serial")
@Subclass(index=true)
public class PinterestData extends BaseInput {

	protected PinterestData(){}
	
	public PinterestData(String userName){
		super(userName);
	}
	
	@Override
	public String getSiteName() {
			return "Pinterest";
	}

	@Override
	String GetFeedURL() {
		return "https://www.pinterest.com/"+userName+"/feed.rss";
	}

	@Override
	BaseFeedParser GetParser() {
		return new PinterestFeedParser(userName);
	}

}
