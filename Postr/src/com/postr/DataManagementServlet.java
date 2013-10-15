package com.postr;

import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
public class DataManagementServlet extends BaseJSONServlet {

	@Override
	protected Result ProcessRequest(String parameters) throws Exception {
		
		Request request = Json.FromJson(parameters, Request.class);

		MethodTypes methodType = MethodTypes.valueOf(request.method);
		switch (methodType) {
		case RemoveData:
			return RemoveData(request);
		default:
			throw new Exception("No such method found: "+request.method);
		}
	}

	private Result RemoveData(Request parameters) {
		Long key = parameters.id;
		DAO.RemoveThing(key);
		return Result.Success("Removed!");
	}

}
