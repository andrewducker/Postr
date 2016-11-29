package com.postr;

import java.util.logging.Logger;

import com.postr.DataTypes.Json;
import com.postr.DataTypes.User;
import com.postr.DataTypes.UserData;

@SuppressWarnings("serial")
public class UserDataServlet extends BaseJSONServlet {

	private static final Logger log = Logger.getLogger(UserDataServlet.class.getName());

	@Override
	protected Result ProcessRequest(Json parameters) throws Exception {
		
		RequestFromURL request = parameters.FromJson(RequestFromURL.class);

		MethodTypes methodType = MethodTypes.valueOf(request.method);
		switch (methodType) {
		case GetData:
			return GetData(request.URL);
		case UpdateData:
			return UpdateData(parameters);
		default:
			throw new Exception("No such method found: "+request.method);
		}
	}

	private Result UpdateData(Json parameters) throws Exception {
		User userWithTimezone = parameters.FromJson(User.class);
		User user = DAO.GetUser(getUserEmail());
		user.timeZone = userWithTimezone.timeZone;
		DAO.SaveUser(user);
		return Result.Success("Updated");
	}

	private Result GetData(String originURL) throws Exception {
		com.postr.DataTypes.UserData userData;
		if (LoggedIn()) {
			log.info("Logged in");
			Long userID = GetUserID();
			String email = getUserEmail();

			 userData = new com.postr.DataTypes.UserData(email, userID);
			
		}else{
			userData = new UserData();
		}
		userData.SetURLs(originURL);
		return new Result("Success",userData);
	}
}
