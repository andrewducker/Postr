
package com.postr;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public abstract class BaseOutputServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
		resp.setContentType("text/plain");

		resp.getWriter().println(VerifyPassword(req));
		} catch (Exception e) {
			e.printStackTrace();
		}

	
	}

	protected abstract String VerifyPassword(HttpServletRequest req) throws Exception;
}
