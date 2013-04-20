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
	
	public Long getLongParameter(String key){
		Double parameter = (Double)parameters.get(key); 
		return parameter.longValue();
	}
}
