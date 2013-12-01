package com.postr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
abstract class BaseJSONServlet extends HttpServlet {
	protected HttpSession session;
	protected String serverName;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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
		try {
			handleRequest(req, resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	protected void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			session = req.getSession();
			serverName = req.getServerName();

			resp.setContentType("application/json");
		    StringBuilder sb = new StringBuilder();
		    String line;
		    while ((line = req.getReader().readLine()) != null) {
		    sb.append(line);
		    }
		    
			Result result = ProcessRequest(new Json(sb.toString()));
			
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
