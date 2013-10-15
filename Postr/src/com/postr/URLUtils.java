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
		}
		catch(Exception e){
			return false;
		}
		return false;

	}
}
