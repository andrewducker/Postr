package com.postr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
abstract class BasePersonaSessionServlet extends HttpServlet {
	private HttpSession session;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			session = req.getSession();
			try {
				handleRequest(req, resp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		session = req.getSession();
		try {
			handleRequest(req, resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	abstract void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception;
	
	protected String GetPersona()
	{
		return (String) session.getAttribute("Persona");
	}
	
	protected boolean LoggedIn(){
		return session.getAttribute("UserID") != null;
	}
	
	
	protected Long GetUserID()
	{
		return (Long) session.getAttribute("UserID");
	}

	protected void SetPersona(String persona) throws Exception
	{
		session.setAttribute("Persona", persona);
		if (persona == null) {
			session.setAttribute("UserID",null);
		}else{
			Long userID = DAO.GetUserID(persona);
			session.setAttribute("UserID",userID);
		}
	}
}
