package com.postr;

import java.util.logging.Logger;

import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
public class AppDataServlet extends BaseJSONServlet {

	private static final Logger log = Logger.getLogger(AppDataServlet.class.getName());

	
	@Override
	protected Result ProcessRequest(Json parameters) throws Exception {
		
		String method = parameters.FromJson(Request.class).method;

		MethodTypes methodType = MethodTypes.valueOf(method);
		switch (methodType) {
		case UpdateData:
			return UpdateData(parameters);
		default:
			throw new Exception("No such method found: "+method);
		}
	}

	private Result UpdateData(Json parameters) throws Exception {
		AppData appData = parameters.FromJson(AppData.class);
		AppData oldAppData = DAO.getAppData();
		
		if (!oldAppData.Administrator.equalsIgnoreCase(getUserEmail())) {
			log.severe("User "+getUserEmail() + " tried to update Site Data illegally.");
			return Result.Failure("Security error - only an administrator should do this.  This security violation has been logged.");
		}
		oldAppData.wordPressClientId = appData.wordPressClientId;
		oldAppData.wordPressClientSecret = appData.wordPressClientSecret;
		DAO.setAppData(oldAppData);
		return Result.Success("Updated");
	}
}
