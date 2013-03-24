package com.postr;

import com.postr.DataTypes.Parameters;
import com.postr.DataTypes.StringResult;
import com.postr.Translators.LJTranslator;

@SuppressWarnings("serial")
public class LivejournalServlet extends BaseOutputServlet {

	@Override
	protected StringResult VerifyPassword(Parameters parameters) throws Exception {
		String userName = parameters.getStringParameter("username");
		String password = parameters.getStringParameter("password");
		LJTranslator writer = new LJTranslator();
		return writer.Login(userName, password);
	}

	@Override
	protected StringResult SaveData(Parameters parameters) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}