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
			try {
				handlePost(req, resp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	abstract void handlePost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception;
	
	protected String GetPersona()
	{
		return (String) session.getAttribute("Persona");
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
