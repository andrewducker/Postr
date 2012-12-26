package com.postr;
import java.io.IOException;
import java.util.TimeZone;

import javax.servlet.http.*;

import com.postr.Writers.LJWriter;

@SuppressWarnings("serial")
public class PostrServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
		resp.setContentType("text/plain");
		String userName = req.getParameter("username");
		String password = req.getParameter("password");
		LJWriter writer = new LJWriter(userName,password, TimeZone.getDefault(), true);
		resp.getWriter().println(writer.Login());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
