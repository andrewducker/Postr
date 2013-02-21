
package com.postr;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;

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
				resp.getWriter().print(VerifyPassword());	
				break;
			default:
				throw new Exception("No such method found: "+method);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	protected abstract Object VerifyPassword() throws Exception;
}
