package com.postr.DataTypes;

import org.joda.time.DateTime;

import com.google.gson.GsonBuilder;

public class Json  {

	private static GsonBuilder builder;

	static	{
		builder = new GsonBuilder();
		builder.registerTypeAdapter(DateTime.class, new DateTimeJsonAdapter());
	}
	
	public static String Convert(Object toConvert){
		return builder.create().toJson(toConvert);
	}
	
	public static <T> T FromJson(String data, Class<T> clazz){
		return builder.create().fromJson(data, clazz);
	} 
	
	private Json(){}
}
