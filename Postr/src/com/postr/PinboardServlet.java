package com.postr;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Input;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Inputs.PinboardData;

@SuppressWarnings("serial")
public class PinboardServlet extends BaseInputServlet {

	@Override
	protected Result UpdateData(String parameters) throws Exception {
		return Result.Failure("There is nothing updateable about a Pinboard Input"); 
	}

	@Override
	protected Result VerifyUserExists(String parameters) throws Exception {
		Input input = Json.FromJson(parameters, Input.class);
		if (URLUtils.DoesURLExist("https://pinboard.in/u:"+input.userName)) {
			return Result.Success("User verified.");
		}
		return Result.Failure("Failed to find user.");
	}

	@Override
	protected Result SaveData(String parameters) throws Exception {
		Input input = Json.FromJson(parameters, Input.class);
		PinboardData pinboardData = new PinboardData(input.userName);
		
		Key<PinboardData> result = DAO.SaveThing(pinboardData,GetUserID());
		return new Result("Saved!",result.getId());
	}

}