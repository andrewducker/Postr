package com.postr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public abstract class BasePersonaSessionServlet extends HttpServlet {
	private HttpSession session;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			session = req.getSession();
			handlePost(req, resp);
	}
	
	abstract void handlePost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException;
	
	protected String GetPersona()
	{
		return (String) session.getAttribute("Persona");
	}

	protected void SetPersona(String persona)
	{
		session.setAttribute("Persona", persona);
	}
	

}
