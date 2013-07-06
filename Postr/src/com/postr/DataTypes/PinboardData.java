package com.postr.DataTypes;

import com.googlecode.objectify.annotation.EntitySubclass;

@EntitySubclass(index=true)
public class PinboardData extends BaseInput {

	protected PinboardData(){}
	
	public PinboardData(String userName){
		super(userName);
	}
	
	@Override
	public String getSiteName() {
			return "Pinboard";
	}

}
