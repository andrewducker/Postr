package com.postr;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Inputs.DeliciousData;

@SuppressWarnings("serial")
public class DeliciousServlet extends BaseInputServlet {

	@Override
	protected Json UpdateData(Json parameters) throws Exception {
		Json toReturn =Json.ErrorResult("There is nothing updateable about a Delicious Input"); 
		return toReturn;
	}

	@Override
	protected Json VerifyUserExists(Json parameters) throws Exception {
		if (URLUtils.DoesURLExist("http://feeds.delicious.com/v2/rss/"+parameters.getString("userName"))) {
			return Json.SuccessResult("User verified.");
		}
		return Json.ErrorResult("Failed to find user.");
	}

	@Override
	protected Json SaveData(Json parameters) throws Exception {
		String userName = parameters.getString("userName");
		DeliciousData deliciousData = new DeliciousData(userName);
		
		Key<DeliciousData> result = DAO.SaveThing(deliciousData,GetUserID());
		Json toReturn = Json.SuccessResult("Saved!");
		toReturn.setData("id", result.getId());
		return toReturn;
	}

}
