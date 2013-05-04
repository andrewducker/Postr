package com.postr;

import java.util.TimeZone;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.LJData;
import com.postr.Translators.LJTranslator;

@SuppressWarnings("serial")
public class LivejournalServlet extends BaseOutputServlet {

	@Override
	protected Json VerifyPassword(Json parameters) throws Exception {
		String userName = parameters.getString("username");
		String password = parameters.getString("password");
		LJTranslator writer = new LJTranslator();
		return writer.Login(userName, password);
	}

	@Override
	protected Json RemoveData(Json parameters) throws Exception {
		Long key = parameters.getLong("key");
		DAO.RemoveThing(LJData.class, key);
		return Json.SuccessResult("Removed!");
	}

	@Override
	protected Json SaveData(Json parameters) throws Exception {
		String userName = parameters.getString("username");
		String password = parameters.getString("password");
		String timeZone = parameters.getString("timezone");
		LJData ljData = new LJData(
				userName, password,
				TimeZone.getTimeZone(timeZone));
		
		Key<LJData> result = DAO.SaveThing(ljData,GetPersona());
		Json toReturn = Json.SuccessResult("Saved!");
		toReturn.setData("key", result.getId());
		return toReturn;
	}
}