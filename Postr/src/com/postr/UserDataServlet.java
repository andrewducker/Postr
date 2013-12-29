package com.postr;

import com.postr.DataTypes.Json;
import com.postr.DataTypes.User;

@SuppressWarnings("serial")
public class UserDataServlet extends BasePersonaSessionServlet {

	@Override
	protected Result ProcessRequest(Json parameters) throws Exception {
		
		String method = parameters.FromJson(Request.class).method;

		MethodTypes methodType = MethodTypes.valueOf(method);
		switch (methodType) {
		case GetData:
			return GetData(parameters);
		case UpdateData:
			return UpdateData(parameters);
		default:
			throw new Exception("No such method found: "+method);
		}
	}

	private Result UpdateData(Json parameters) {
		User user = parameters.FromJson(User.class);
		SetTimeZone(user.timeZone);
		SaveUserData();
		return Result.Success("Updated");
	}

	private Result GetData(Json parameters) {
		if (LoggedIn()) {
			Long userID = GetUserID();
			String persona = GetPersona();

			com.postr.DataTypes.UserData userData = new com.postr.DataTypes.UserData(persona, userID);
			
			return new Result("UserData available", userData);
		}
		return Result.Success("");
	}
}
