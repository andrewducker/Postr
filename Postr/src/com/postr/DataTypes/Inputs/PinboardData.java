package com.postr.DataTypes.Inputs;

import com.googlecode.objectify.annotation.Subclass;

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

}
