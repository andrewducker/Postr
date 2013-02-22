package com.postr;

import com.postr.DataTypes.StringResult;
import com.postr.Translators.DWTranslator;

@SuppressWarnings("serial")
public class DreamwidthServlet extends BaseOutputServlet {

	@Override
	protected StringResult VerifyPassword() throws Exception {
		String userName = getStringParameter("username");
		String password = getStringParameter("password");
		DWTranslator writer = new DWTranslator();
		return writer.Login(userName, password);
	}

	@Override
	protected StringResult SaveData() throws Exception {
		String userName = getStringParameter("username");
		String password = getStringParameter("password");
		return StringResult.SuccessResult("Saved!");
	}

}
