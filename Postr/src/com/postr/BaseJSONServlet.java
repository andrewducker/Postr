package com.postr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
abstract class BaseJSONServlet extends BasePersonaSessionServlet {
	
	@Override
	protected void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			resp.setContentType("text/plain");
			String params = req.getParameter("params");
			
			Result result = ProcessRequest(new Json(params));
			
			processResult(resp, result);
		} catch (Exception e) {
			resp.setStatus(500);
			resp.getWriter().print(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void processResult(HttpServletResponse resp, Result result)
			throws IOException, Exception {
		if (result.failure) {
			resp.setStatus(500);
			resp.getWriter().print(result.message);
		}else
		{
			resp.getWriter().print(new Json(result).getJson());	
		}
	}
	
	protected abstract Result ProcessRequest(Json parameters) throws Exception;
}
