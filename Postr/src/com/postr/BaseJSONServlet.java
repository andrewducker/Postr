package com.postr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.postr.DataTypes.Json;
import com.postr.DataTypes.ThreadStorage;

@SuppressWarnings("serial")
abstract class BaseJSONServlet extends HttpServlet {
	protected String serverName;
	
	private ThreadLocal<HttpSession> session = new ThreadLocal<HttpSession>();
	private ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	private ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();
	
	protected HttpSession getSession() {
		return session.get();
	}

	private void setSession(HttpSession value) {
		session.set(value);
	}

	protected HttpServletRequest getRequest() {
		return request.get();
	}

	private void setRequest(HttpServletRequest value) {
		request.set(value);
	}

	protected HttpServletResponse getResponse() {
		return response.get();
	}

	private void setResponse(HttpServletResponse value) {
		response.set(value);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			try {
				handleRequest(req, resp);
			} catch (Exception e) {
				LogHandler.logException(this.getClass(), e, "Uncaught Exception");
			}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			handleRequest(req, resp);
		} catch (Exception e) {
			LogHandler.logException(this.getClass(), e, "Uncaught Exception");
		}
	}

	protected void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			setSession(req.getSession());
			setRequest(req);
			setResponse(resp);
			serverName = req.getServerName();

			resp.setContentType("application/json");
		    StringBuilder sb = new StringBuilder();
		    String line;
		    while ((line = req.getReader().readLine()) != null) {
		    sb.append(line);
		    }
		    
		    LogHandler.info("Request - " + sb.toString());
		    
		    InitialiseProcessing();
		    
			Result result = ProcessRequest(new Json(sb.toString()));
			
			processResult(resp, result);
		} catch (Exception e) {
			resp.setStatus(500);
			resp.getWriter().print(e.getMessage());
			LogHandler.logException(this.getClass(), e, "Uncaught Exception");
		} finally {
			ThreadStorage.ClearAll();
			session.remove();
		}
	}
	
	void InitialiseProcessing() throws Exception{
	}
	
	protected void AddCookie(Cookie cookie){
		getResponse().addCookie(cookie);
	}
	
	protected Cookie[] GetCookies(){
		return getRequest().getCookies();
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
