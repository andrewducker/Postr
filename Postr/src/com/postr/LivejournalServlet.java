package com.postr;

import com.postr.DataTypes.StringResult;
import com.postr.Translators.LJTranslator;

@SuppressWarnings("serial")
public class LivejournalServlet extends BaseOutputServlet {

	@Override
	protected StringResult VerifyPassword() throws Exception {
		String userName = getStringParameter("username");
		String password = getStringParameter("password");
		LJTranslator writer = new LJTranslator();
		return writer.Login(userName, password);
	}

	@Override
	protected StringResult SaveData() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}