package com.postr;

import javax.servlet.http.HttpServletResponse;

import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
abstract class BaseJSONServlet extends BaseUserSessionServlet {

	@Override
	protected void handleRequest() throws Exception {
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = getRequest().getReader().readLine()) != null) {
			sb.append(line);
		}

		LogHandler.logInfo(this,"Request - " + sb.toString());

		Result result = ProcessRequest(new Json(sb.toString()));
		
		HttpServletResponse resp = getResponse();
		if (result.failure) {
			resp.setStatus(500);
			resp.getWriter().print(result.message);
		} else {
			resp.setContentType("application/json");
			resp.getWriter().print(new Json(result).getJson());
		}
	}

	protected abstract Result ProcessRequest(Json parameters) throws Exception;
}
