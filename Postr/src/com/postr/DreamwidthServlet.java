package com.postr;

import com.postr.DataTypes.Parameters;
import com.postr.DataTypes.StringResult;
import com.postr.Translators.DWTranslator;

@SuppressWarnings("serial")
public class DreamwidthServlet extends BaseOutputServlet {

	@Override
	protected StringResult VerifyPassword(Parameters parameters) throws Exception {
		String userName = parameters.getStringParameter("username");
		String password = parameters.getStringParameter("password");
		DWTranslator writer = new DWTranslator();
		return writer.Login(userName, password);
	}

	@Override
	protected StringResult SaveData(Parameters parameters) throws Exception {
		String userName = parameters.getStringParameter("username");
		String password = parameters.getStringParameter("password");
		return StringResult.SuccessResult("Saved!");
	}

}
