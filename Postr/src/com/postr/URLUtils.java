package com.postr;

import java.net.HttpURLConnection;
import java.net.URL;

class URLUtils {
	static Boolean DoesURLExist(String urlAddress){
		try{
			URL url = new URL(urlAddress);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			int code = connection.getResponseCode();
			if (code == 200) {
				return true;			
			}
			LogHandler.info("Failed to find URL - " + urlAddress + ". Code - " + code + "Current IP is - " + LogHandler.CurrentIP());
		}
		catch(Exception e){
			LogHandler.logException(URLUtils.class, e, "Failed to find URL - " + urlAddress);
			return false;
		}
		return false;

	}
}
