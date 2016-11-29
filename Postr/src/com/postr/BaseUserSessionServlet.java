package com.postr;

import com.google.appengine.api.users.UserServiceFactory;
import com.postr.DataTypes.ThreadStorage;
import com.postr.DataTypes.User;

@SuppressWarnings("serial")
abstract class BaseUserSessionServlet extends BaseServlet {

	protected String getUserEmail() {
		return UserServiceFactory.getUserService().getCurrentUser().getEmail();
	}

	protected boolean LoggedIn() {
		return getRequest().getUserPrincipal() != null;
	}

	protected Long GetUserID() throws Exception {
		return DAO.GetUser(getUserEmail()).getId();
	}

	protected String GetTimeZone() throws Exception {
		return DAO.GetUser(getUserEmail()).timeZone;
	}

	@Override
	void InitialiseProcessing() throws Exception {
		// Are we logged in already?
		if (LoggedIn()) {
			ThreadStorage.setDateTimeZone(GetTimeZone());
		}
	}
}
