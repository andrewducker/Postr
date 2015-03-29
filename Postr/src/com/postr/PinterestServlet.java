package com.postr;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Input;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Inputs.PinterestData;

@SuppressWarnings("serial")
public class PinterestServlet extends BaseInputServlet {

	@Override
	protected Result UpdateData(Json parameters) throws Exception {
		return Result.Failure("There is nothing updateable about a Pinterest Input"); 
	}

	@Override
	protected Result VerifyUserExists(Json parameters) throws Exception {
		Input input = parameters.FromJson(Input.class);
		if (URLUtils.DoesURLExist("https://www.pinterest.com/"+input.userName)) {
			return Result.Success("User verified.");
		}
		return Result.Failure("Failed to find user.");
	}

	@Override
	protected Result SaveData(Json parameters) throws Exception {
		Input input = parameters.FromJson(Input.class);
		String userName = input.userName;
		PinterestData data = new PinterestData(userName);
		
		Key<PinterestData> result = DAO.SaveThing(data,GetUserID());
		return new Result("Saved!",result.getId());
	}

}
