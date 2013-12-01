package com.postr;

import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
public class UserDataServlet extends BasePersonaSessionServlet {

	
	@Override
	protected Result ProcessRequest(Json parameters) throws Exception {
		if (LoggedIn()) {
			Long userID = GetUserID();
			String persona = GetPersona();

			com.postr.DataTypes.UserData userData = new com.postr.DataTypes.UserData(persona, userID);
			
			return new Result("UserData available", userData);
		}
		return Result.Success("");
	}

}
