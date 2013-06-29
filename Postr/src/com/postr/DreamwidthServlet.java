package com.postr;

import java.util.TimeZone;

import com.postr.DataTypes.DWData;
import com.postr.DataTypes.Json;
import com.postr.Translators.DWTranslator;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class DreamwidthServlet extends BaseOutputServlet {

	@Override
	protected Json VerifyPassword(Json parameters) throws Exception {
		String userName = parameters.getString("username");
		String password = parameters.getString("password");
		DWTranslator writer = new DWTranslator();
		return writer.Login(userName, password);
	}	

	@Override
	protected Json SaveData(Json parameters) throws Exception {
		String userName = parameters.getString("username");
		String password = parameters.getString("password");
		String timeZone = parameters.getString("timezone");
		DWData dwData = new DWData(
				userName, password,
				TimeZone.getTimeZone(timeZone));
		
		Key<DWData> result = DAO.SaveThing(dwData,GetPersona());
		Json toReturn =Json.SuccessResult("Saved!"); 
		toReturn.setData("key", result.getId());
		return toReturn;
	}

	@Override
	protected Json UpdateData(Json parameters) throws Exception {
		Long key = parameters.getLong("key");
		String password = parameters.getString("password");
		String timeZone = parameters.getString("timezone");
		DWData existingDWData = DAO.LoadThing(DWData.class, key);
		
		DWData newDWData = new DWData(existingDWData,password,TimeZone.getTimeZone(timeZone));
		
		Key<DWData> result = DAO.SaveThing(newDWData,GetPersona());
		Json toReturn =Json.SuccessResult("Saved!"); 
		toReturn.setData("key", result.getId());
		return toReturn;
	}
}
