package com.postr;

import javax.servlet.http.HttpServlet;

public class PersonaTestServlet extends HttpServlet {

	@Override
	protected void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) 
			throws javax.servlet.ServletException ,java.io.IOException {
		resp.setContentType("text/html");
		String email = (String) req.getSession().getAttribute("EmailAddress");
		String javascriptEmail = "null";
		resp.getWriter().write("<!DOCTYPE html><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\"> <title>Persona Test</title></head><body>");
		if (email != null) {
			resp.getWriter().write("Logged in as: " + email + "<BR>");
			javascriptEmail = "'"+email+"'";
		}
		
		
		resp.getWriter().write("<button type=\"button\" id=signin onclick = \"navigator.id.request()\" }>Sign In!</button><button type=\"button\" id=signout onclick= \"navigator.id.logout()\">Sign Out!</button>"+"\n" + " <script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js\"></script><script src=\"https://login.persona.org/include.js\"></script>"+"\n" + "<script>var currentUser = "+javascriptEmail+"; navigator.id.watch({  loggedInUser: currentUser,  onlogin: function(assertion) {   "+"\n" + "$.ajax({  type: 'POST',	        url: '/personaverification', data: {assertion: assertion},	        success: function(res, status, xhr) { alert(\"success!\"); },	        "+"\n" + "error: function(xhr, status, err) {	          "+"\n" + "navigator.id.logout();	          "+"\n" + "alert(\"Login failure: \" + err);	        }	      });  },"+"\n" + "  onlogout: function() {location.reload()}});</script></body></html> ");
	};
	
}
