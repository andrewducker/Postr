package com.postr;

import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
public class UserLogoutServlet extends BaseJSONServlet {
	
	@Override
	protected Result ProcessRequest(Json parameters) throws Exception {
			setUserEmail(null);
			return Result.Success("Logged out");
	}
}
