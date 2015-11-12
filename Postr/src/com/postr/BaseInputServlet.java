package com.postr;

import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
abstract class BaseInputServlet extends BaseJSONServlet {

	@Override
	protected Result ProcessRequest(Json parameters) throws Exception {
		Request request = parameters.FromJson(Request.class);

		MethodTypes methodType = MethodTypes.valueOf(request.method);
		switch (methodType) {
		case SaveData:
			return SaveData(parameters);
		case Verify:
			return VerifyUserExists(parameters);
		case UpdateData:
			return UpdateData(parameters);
		default:
			throw new Exception("No such method found: "+request.method);
		}
	}
	
	protected abstract Result UpdateData(Json parameters) throws Exception;

	protected abstract Result VerifyUserExists(Json parameters) throws Exception;
	
	protected abstract Result SaveData(Json parameters) throws Exception;
}
