
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

			String method = req.getParameter("method");

			MethodTypes methodType = MethodTypes.valueOf(method);

			switch (methodType) {
			case VerifyPassword:
				resp.getWriter().print(VerifyPassword(req));	
				break;
			default:
				throw new Exception("No such method found: "+method);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	protected abstract String VerifyPassword(HttpServletRequest req) throws Exception;
}
