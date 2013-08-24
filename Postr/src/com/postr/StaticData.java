package com.postr;

import java.util.TimeZone;
import java.util.TreeSet;

public class StaticData {

	private static TreeSet<String> timeZones = new TreeSet<String>();
	
	static {
		for (String timeZone : TimeZone.getAvailableIDs()) {
			timeZones.add(timeZone);
		}
	}

	public Iterable<String> getTimeZones(){
		return timeZones;
	}

}
