package com.postr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
public class UserDataServlet extends BasePersonaSessionServlet {

	
	@Override
	void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		resp.setContentType("text/plain");
		
		if (LoggedIn()) {
			Long userID = GetUserID();
			String persona = GetPersona();

			com.postr.DataTypes.UserData userData = new com.postr.DataTypes.UserData(persona, userID);
			
			resp.getWriter().write(Json.Convert(userData));
		}
	}

}
