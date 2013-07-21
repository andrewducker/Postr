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
		String userName = parameters.getString("userName");
		String password = parameters.getString("password");
		LJTranslator writer = new LJTranslator();
		return writer.Login(userName, password);
	}

	@Override
	protected Json SaveData(Json parameters) throws Exception {
		String userName = parameters.getString("userName");
		String password = parameters.getString("password");
		String timeZone = parameters.getString("timeZone");
		LJData ljData = new LJData(
				userName, password,
				TimeZone.getTimeZone(timeZone));
		
		Key<LJData> result = DAO.SaveThing(ljData,GetUserID());
		Json toReturn = Json.SuccessResult("Saved!");
		toReturn.setData("key", result.getId());
		return toReturn;
	}

	@Override
	protected Json UpdateData(Json parameters) throws Exception {
		Long key = parameters.getLong("key");
		String password = parameters.getString("password");
		String timeZone = parameters.getString("timeZone");
		LJData existingLJData = DAO.LoadThing(LJData.class, key);
		
		LJData newDWData = new LJData(existingLJData,password,TimeZone.getTimeZone(timeZone));
		
		Key<LJData> result = DAO.SaveThing(newDWData,GetUserID());
		Json toReturn =Json.SuccessResult("Saved!"); 
		toReturn.setData("key", result.getId());
		return toReturn;
	}
}