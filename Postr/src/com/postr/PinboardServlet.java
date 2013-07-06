package com.postr;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.PinboardData;

@SuppressWarnings("serial")
public class PinboardServlet extends BaseInputServlet {

	@Override
	protected Json UpdateData(Json parameters) throws Exception {
		Json toReturn =Json.ErrorResult("There is nothing updateable about a Pinboard Input"); 
		return toReturn;
	}

	@Override
	protected Json VerifyUserExists(Json parameters) throws Exception {
		if (URLUtils.DoesURLExist("https://pinboard.in/u:"+parameters.getString("userName"))) {
			return Json.SuccessResult("User verified.");
		}
		return Json.ErrorResult("Failed to find user.");
	}

	@Override
	protected Json SaveData(Json parameters) throws Exception {
		String userName = parameters.getString("userName");
		PinboardData pinboardData = new PinboardData(userName);
		
		Key<PinboardData> result = DAO.SaveThing(pinboardData,GetPersona());
		Json toReturn = Json.SuccessResult("Saved!");
		toReturn.setData("key", result.getId());
		return toReturn;
	}

}