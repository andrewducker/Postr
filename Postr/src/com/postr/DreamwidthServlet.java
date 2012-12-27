package com.postr;

import javax.servlet.http.HttpServletRequest;

import com.postr.Writers.DWWriter;
import com.postr.Writers.LJWriter;

@SuppressWarnings("serial")
public class DreamwidthServlet extends BaseOutputServlet {

	@Override
	protected String VerifyPassword(HttpServletRequest req) throws Exception {
		String userName = req.getParameter("username");
		String password = req.getParameter("password");
		DWWriter writer = new DWWriter();
		return writer.Login(userName, password);
	}

}
