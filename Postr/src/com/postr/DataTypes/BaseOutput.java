package com.postr.DataTypes;

import com.googlecode.objectify.annotation.EntitySubclass;

@EntitySubclass(index=true)
public class BaseOutput extends BaseSaveable {

	public String getDescription(){
		return "Needs to be implemented";
	}
	
}