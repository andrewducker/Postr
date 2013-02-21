package com.postr;

import com.postr.Translators.LJTranslator;

@SuppressWarnings("serial")
public class LivejournalServlet extends BaseOutputServlet {

	@Override
	protected String VerifyPassword() throws Exception {
		String userName = getStringParameter("username");
		String password = getStringParameter("password");
		LJTranslator writer = new LJTranslator();
		return writer.Login(userName, password);
	}
}