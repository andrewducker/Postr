package com.postr.DataTypes.Inputs;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.BaseFeedParser;
import com.postr.DeliciousFeedParser;

@SuppressWarnings("serial")
@Subclass(index=true)
public class DeliciousData extends BaseInput {

	protected DeliciousData(){}
	
	public DeliciousData(String userName){
		super(userName);
	}
	
	@Override
	public String getSiteName() {
			return "Delicious"; 
	}

	@Override
	String GetFeedURL() {
		return "http://premium-feeds-preview.delicious.com/v2/rss/"+userName+"?count=30";
	}

	@Override
	BaseFeedParser GetParser() {
		return new DeliciousFeedParser(userName);
	}

}
