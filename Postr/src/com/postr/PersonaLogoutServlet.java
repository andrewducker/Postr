package com.postr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PersonaLogoutServlet extends BasePersonaSessionServlet {
	protected void handlePost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
    	SetPersona(null); 
	}
}