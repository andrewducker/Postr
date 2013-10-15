package com.postr;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Input;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Inputs.DeliciousData;

@SuppressWarnings("serial")
public class DeliciousServlet extends BaseInputServlet {

	@Override
	protected Result UpdateData(String parameters) throws Exception {
		return Result.Failure("There is nothing updateable about a Delicious Input"); 
	}

	@Override
	protected Result VerifyUserExists(String parameters) throws Exception {
		Input input = Json.FromJson(parameters, Input.class);
		if (URLUtils.DoesURLExist("http://feeds.delicious.com/v2/rss/"+input.userName)) {
			return Result.Success("User verified.");
		}
		return Result.Failure("Failed to find user.");
	}

	@Override
	protected Result SaveData(String parameters) throws Exception {
		Input input = Json.FromJson(parameters, Input.class);
		String userName = input.userName;
		DeliciousData deliciousData = new DeliciousData(userName);
		
		Key<DeliciousData> result = DAO.SaveThing(deliciousData,GetUserID());
		return new Result("Saved!",result.getId());
	}

}
