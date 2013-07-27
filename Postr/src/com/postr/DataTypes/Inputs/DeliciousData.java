package com.postr.DataTypes.Inputs;

import com.googlecode.objectify.annotation.EntitySubclass;

@EntitySubclass(index=true)
public class DeliciousData extends BaseInput {

	protected DeliciousData(){}
	
	public DeliciousData(String userName){
		super(userName);
	}
	
	@Override
	public String getSiteName() {
			return "Delicious";
	}

}
