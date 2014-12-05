package com.postr;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Input;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Inputs.TestInputData;

@SuppressWarnings("serial")
public class TestInputServlet extends BaseInputServlet {

	@Override
	protected Result UpdateData(Json parameters) throws Exception {
		return Result.Failure("There is nothing updateable about a Test Input"); 
	}

	@Override
	protected Result VerifyUserExists(Json parameters) throws Exception {
		return Result.Success("User verified (test).");
	}

	@Override
	protected Result SaveData(Json parameters) throws Exception {
		Input input = parameters.FromJson(Input.class);
		String userName = input.userName;
		TestInputData testInputData = new TestInputData(userName);
		
		Key<TestInputData> result = DAO.SaveThing(testInputData,GetUserID());
		return new Result("Saved!",result.getId());
	}

}
