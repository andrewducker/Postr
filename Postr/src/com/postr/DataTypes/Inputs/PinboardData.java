package com.postr.DataTypes.Inputs;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.BaseFeedParser;
import com.postr.PinboardFeedParser;

@Subclass(index=true)
public class PinboardData extends BaseInput {

	protected PinboardData(){}
	
	public PinboardData(String userName){
		super(userName);
	}
	
	@Override
	public String getSiteName() {
			return "Pinboard";
	}

	@Override
	BaseFeedParser GetParser() {
		return new PinboardFeedParser(userName);
	}

	@Override
	String GetFeedURL() {
		return "http://feeds.pinboard.in/rss/u:"+userName;	
		}
}
