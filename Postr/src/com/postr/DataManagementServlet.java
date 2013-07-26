package com.postr;

import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
public class DataManagementServlet extends BaseJSONServlet {

	@Override
	protected Json ProcessRequest(Json parameters) throws Exception {
		String method = parameters.getString("method");

		MethodTypes methodType = MethodTypes.valueOf(method);
		switch (methodType) {
		case RemoveData:
			return RemoveData(parameters);
		default:
			throw new Exception("No such method found: "+method);
		}
	}

	private Json RemoveData(Json parameters) {
		Long key = parameters.getLong("key");
		DAO.RemoveThing(key, GetUserID());
		return Json.SuccessResult("Removed!");
	}

}
