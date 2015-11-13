package com.postr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.logging.Logger;

public class LogHandler {

	public static String CurrentIP() throws Exception{
		
        URL url = new URL("http://www.realip.info/api/p/realip.php");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
        	sb.append(line);
        }
        reader.close();
        String currentIP = sb.toString();
		return currentIP;
	}
	
	public static void logException(Object caller, Exception e, String message)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		
		getLogger(caller).severe(message + " : " + sw.toString());
	}
	
	public static <T> void logWarning(Object caller, String message){
		 getLogger(caller).warning(message);
	}
	
	@SuppressWarnings("rawtypes")
	private static Logger getLogger(Object caller){
		String className;
		if(caller instanceof Class){
			className = ((Class)caller).getName();
		}else if (caller instanceof String){
			className = (String) caller;
		}else {
			className = caller.getClass().getName();
		}
		return Logger.getLogger(className);
		
	}

	public static void logInfo(Object caller,String message)
	{
		getLogger(caller).info(message);
	}

	public static void logSevere(Object caller, String message ){
		getLogger(caller).severe(message);
	}
	
}
