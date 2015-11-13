package com.postr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.postr.DataTypes.ThreadStorage;

@SuppressWarnings("serial")
public abstract class BaseServlet extends HttpServlet {
	static {
		DAO.EnsureRegistration();
	}
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
	
	protected void AddCookie(Cookie cookie){
		getResponse().addCookie(cookie);
	}
	
	protected Cookie[] GetCookies(){
		return getRequest().getCookies();
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			try {
				handleRequest(req, resp);
			} catch (Exception e) {
				LogHandler.logException(this, e, "Uncaught Exception");
			}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			handleRequest(req, resp);
		} catch (Exception e) {
			LogHandler.logException(this, e, "Uncaught Exception");
		}
	}
	protected void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			setSession(req.getSession());
			setRequest(req);
			setResponse(resp);
			serverName = req.getServerName();
	    
		    InitialiseProcessing();
		    
		    handleRequest();
		    
		} catch (Exception e) {
			resp.setStatus(500);
			resp.getWriter().print(e.getMessage());
			LogHandler.logException(this, e, "Uncaught Exception");
		} finally {
			ThreadStorage.ClearAll();
			session.remove();
		}
	}
	void InitialiseProcessing() throws Exception{
	}
	abstract void handleRequest() throws Exception;


}
