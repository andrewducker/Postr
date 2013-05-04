package com.postr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
public abstract class BaseJSONServlet extends BasePersonaSessionServlet {
	
	@Override
	protected void handlePost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			resp.setContentType("text/plain");
			String params = req.getParameter("params");
			Json parameters = new Json(params);
			
			Json result = ProcessRequest(parameters);
			
			processResult(resp, result);


		} catch (Exception e) {
			resp.setStatus(500);
			resp.getWriter().print(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void processResult(HttpServletResponse resp, Json result)
			throws IOException, Exception {
		if (result.getString(Json.RESULT) != null) {
			resp.getWriter().print(result.ToJson());	
		}
		else
		{
			resp.setStatus(500);
			resp.getWriter().print(result.getString(Json.ERROR_MESSAGE));	
		}
	}
	
	protected abstract Json ProcessRequest(Json parameters) throws Exception;
}
