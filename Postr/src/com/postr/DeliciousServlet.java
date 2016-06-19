package com.postr;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Input;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Inputs.DeliciousData;

@SuppressWarnings("serial")
public class DeliciousServlet extends BaseInputServlet {

	@Override
	protected Result UpdateData(Json parameters) throws Exception {
		return Result.Failure("There is nothing updateable about a Delicious Input"); 
	}

	@Override
	protected Result VerifyUserExists(Json parameters) throws Exception {
		Input input = parameters.FromJson(Input.class);
		if (URLUtils.DoesURLExist("http://feeds.del.icio.us/v2/rss/"+input.userName)) {
			return Result.Success("User verified.");
		}
		return Result.Failure("Failed to find user.");
	}

	@Override
	protected Result SaveData(Json parameters) throws Exception {
		Input input = parameters.FromJson(Input.class);
		String userName = input.userName;
		DeliciousData deliciousData = new DeliciousData(userName);
		
		Key<DeliciousData> result = DAO.SaveThing(deliciousData,GetUserID());
		return new Result("Saved!",result.getId());
	}

}
