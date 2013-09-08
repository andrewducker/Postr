package com.postr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UserDataServlet extends BasePersonaSessionServlet {

	@Override
	void handlePost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		resp.setContentType("text/plain");
		

	}

}
