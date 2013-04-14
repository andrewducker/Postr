package com.postr;

import javax.servlet.http.HttpSession;

public class UserData {
	public String getPersona(){
		return persona;
	}
	
	private String persona;
	
	public void setSession(HttpSession session){
		persona = (String)session.getAttribute("Persona");
		if (persona == null) {
			persona = "Not logged in";
		}
	}
}
