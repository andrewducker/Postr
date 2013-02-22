
package com.postr;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.postr.DataTypes.StringResult;

@SuppressWarnings("serial")
public abstract class BaseOutputServlet extends HttpServlet {

	StringMap<Object> parameters;
	
	protected String getStringParameter(String key){
		return (String)parameters.get(key);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			resp.setContentType("text/plain");
			
			String params = req.getParameter("params");
			parameters = (StringMap<Object>)new Gson().fromJson(params, Object.class);

			String method = getStringParameter("method");

			MethodTypes methodType = MethodTypes.valueOf(method);

			switch (methodType) {
			case VerifyPassword:
				processResult(resp, VerifyPassword());
				break;
			default:
				throw new Exception("No such method found: "+method);
			}


		} catch (Exception e) {
			resp.setStatus(500);
			resp.getWriter().print(e.getMessage());
			e.printStackTrace();
		}


	}

	private void processResult(HttpServletResponse resp, StringResult result)
			throws IOException, Exception {
		if (result.getResult() != null) {
			resp.getWriter().print(new Gson().toJson(result));	
		}
		else
		{
			resp.setStatus(500);
			resp.getWriter().print(result.getErrorMessage());	
		}
	}

	protected abstract StringResult VerifyPassword() throws Exception;
}