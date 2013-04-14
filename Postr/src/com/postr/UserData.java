package com.postr;

import javax.servlet.http.HttpServletRequest;

public class UserData {
	public String getPersona(){
		return persona;
	}
	
	private String persona;
	
	

	public void setRequest(HttpServletRequest request) {
		persona = (String)request.getSession().getAttribute("Persona");
		if (persona == null) {
			persona = "Not logged in";
		}

	}
}
