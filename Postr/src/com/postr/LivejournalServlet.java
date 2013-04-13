package com.postr;

import java.util.TimeZone;

import com.postr.DataTypes.LJData;
import com.postr.DataTypes.Parameters;
import com.postr.DataTypes.StringResult;
import com.postr.Translators.LJTranslator;

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
		String userName = parameters.getStringParameter("username");
		String password = parameters.getStringParameter("password");
		String timeZone = parameters.getStringParameter("timezone");
		LJData ljData = new LJData(
				userName, password,
				TimeZone.getTimeZone(timeZone));
		
		DAO.SaveThing(ljData,GetPersona());
		return StringResult.SuccessResult("Saved!");
	}
}