package com.postr;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.postr.DataTypes.Json;
import com.postr.DataTypes.ThreadStorage;

@SuppressWarnings("serial")
abstract class BaseJSONServlet extends HttpServlet {
	protected HttpSession session;
	protected String serverName;
	
	private static final Logger log = Logger.getLogger(BaseJSONServlet.class.getName());

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			try {
				handleRequest(req, resp);
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);

				e.printStackTrace(pw);
				log.severe("Uncaught Exception: " + sw.toString());
			}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			handleRequest(req, resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);

			e.printStackTrace(pw);
			log.severe("Uncaught Exception: " + sw.toString());

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
		    
		    log.warning("Request - " + sb.toString());
		    
		    InitialiseProcessing();
		    
			Result result = ProcessRequest(new Json(sb.toString()));
			
			processResult(resp, result);
		} catch (Exception e) {
			resp.setStatus(500);
			resp.getWriter().print(e.getMessage());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.severe("Uncaught Exception: " + sw.toString());
		} finally {
			ThreadStorage.ClearAll();
		}
	}
	
	void InitialiseProcessing(){
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
