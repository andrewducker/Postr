package com.postr.DataTypes;

import com.google.gson.internal.StringMap;

public class Parameters {

	private StringMap<Object> parameters;
	public Parameters(StringMap<Object> parameters){
		this.parameters = parameters;
	}
	
	public String getStringParameter(String key){
		Object parameter = parameters.get(key); 
		return (String)parameter;
	}
}
