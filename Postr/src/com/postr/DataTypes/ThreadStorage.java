package com.postr.DataTypes;

import org.joda.time.DateTimeZone;

public class ThreadStorage {

	private static ThreadLocal<DateTimeZone> dateTimeZone = new ThreadLocal<DateTimeZone>();
	
	public static DateTimeZone getDateTimeZone(){
		return dateTimeZone.get();
	}
	
	public static void setDateTimeZone(String value){
		dateTimeZone.set(DateTimeZone.forID(value));
	}
	
	public static void ClearAll(){
		dateTimeZone.remove();
	}
	
}
