package com.postr.DataTypes;

import org.joda.time.DateTime;

import com.google.gson.GsonBuilder;

public class Json  {

	private String data;
	
	public String getJson(){
		return data;
	}
	
	public Json(String data){
		this.data = data;
	}
	
	public Json(Object data){
		this.data = builder.create().toJson(data);
	}
	
	public <T> T FromJson(Class<T> clazz){
		return builder.create().fromJson(data,clazz);
	} 
	
	private static GsonBuilder builder;

	static	{
		builder = new GsonBuilder();
		builder.registerTypeAdapter(DateTime.class, new DateTimeJsonAdapter());
	}
}
