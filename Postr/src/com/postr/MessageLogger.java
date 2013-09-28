package com.postr;

import java.util.logging.Logger;

public class MessageLogger {
		public static void Severe(Object caller, String message ){
			Logger log = Logger.getLogger(caller.getClass().getName());
			log.severe(message);
		}
}
