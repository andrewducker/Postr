package com.postr;

import javax.servlet.http.HttpServletRequest;

import com.postr.Translators.LJTranslator;

@SuppressWarnings("serial")
public class LivejournalServlet extends BaseOutputServlet {

	@Override
	protected String VerifyPassword(HttpServletRequest req) throws Exception {
		String userName = req.getParameter("username");
		String password = req.getParameter("password");
		LJTranslator writer = new LJTranslator();
		return writer.Login(userName, password);
	}
}