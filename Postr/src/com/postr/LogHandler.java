package com.postr;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

public class LogHandler {

	public static void logException(String location, Exception e, String message)
	{
		Logger log = Logger.getLogger(location);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		e.printStackTrace(pw);
		log.severe(message + " : " + sw.toString());
	}
	
	public static <T> void logException(Class<T> clazz, Exception e, String message)
	{
		logException(clazz.getName(), e, message);
	}

	
	static Logger infoLogger = Logger.getLogger("info");
	
	public static void info(String message)
	{
		infoLogger.info(message);
	}
	
}
