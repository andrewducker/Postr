package com.postr;

import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
abstract class BaseInputServlet extends BaseJSONServlet {

	@Override
	protected Result ProcessRequest(String parameters) throws Exception {
		Request request = Json.FromJson(parameters, Request.class);

		MethodTypes methodType = MethodTypes.valueOf(request.method);
		switch (methodType) {
		case SaveData:
			return SaveData(parameters);
		case VerifyUserExists:
			return VerifyUserExists(parameters);
		case UpdateData:
			return UpdateData(parameters);
		default:
			throw new Exception("No such method found: "+request.method);
		}
	}
	
	protected abstract Result UpdateData(String parameters) throws Exception;

	protected abstract Result VerifyUserExists(String parameters) throws Exception;
	
	protected abstract Result SaveData(String parameters) throws Exception;
}
