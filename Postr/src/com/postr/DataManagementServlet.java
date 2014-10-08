package com.postr;

import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
public class DataManagementServlet extends BasePersonaSessionServlet {

	@Override
	protected Result ProcessRequest(Json parameters) throws Exception {
		
		Request request = parameters.FromJson(Request.class);

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
