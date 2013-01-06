package com.postr;

import javax.servlet.http.HttpServletRequest;

import com.postr.Translators.DWTranslator;
import com.postr.Translators.LJTranslator;

@SuppressWarnings("serial")
public class DreamwidthServlet extends BaseOutputServlet {

	@Override
	protected String VerifyPassword(HttpServletRequest req) throws Exception {
		String userName = req.getParameter("username");
		String password = req.getParameter("password");
		DWTranslator writer = new DWTranslator();
		return writer.Login(userName, password);
	}

}
