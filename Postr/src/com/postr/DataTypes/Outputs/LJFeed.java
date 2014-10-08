package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.LivejournalVisibilityTypes;

@Subclass(index = true)
public class LJFeed extends BaseFeed {

	String tags;
	LivejournalVisibilityTypes visibility;
	Boolean autoFormat;
	public String timeZone;
	
	@Override
	void MakePost() {
		// TODO Auto-generated method stub

	}

}
