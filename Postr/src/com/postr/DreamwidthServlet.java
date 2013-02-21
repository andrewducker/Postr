package com.postr;

import com.postr.Translators.DWTranslator;

@SuppressWarnings("serial")
public class DreamwidthServlet extends BaseOutputServlet {

	@Override
	protected String VerifyPassword() throws Exception {
		String userName = getStringParameter("username");
		String password = getStringParameter("password");
		DWTranslator writer = new DWTranslator();
		return writer.Login(userName, password);
	}

}
