package com.postr;

import com.postr.DataTypes.ThreadStorage;
import com.postr.DataTypes.User;

@SuppressWarnings("serial")
abstract class BaseUserSessionServlet extends BaseServlet {

	protected String getUserEmail() {
		return (String) getSession().getAttribute("Email");
	}

	protected boolean LoggedIn() {
		if (getSession().getAttribute("User") != null) {
			return true;
		}
		return false;
	}

	protected Long GetUserID() {
		User user = (User) getSession().getAttribute("User");
		return user.getId();
	}

	protected String GetTimeZone() {
		return ((User) getSession().getAttribute("User")).timeZone;
	}

	protected void SetTimeZone(String timeZone) {
		((User) getSession().getAttribute("User")).timeZone = timeZone;
	}

	protected void SaveUserData() {
		DAO.SaveUser(((User) getSession().getAttribute("User")));
	}

	protected void setUserEmail(String email) throws Exception {
		String oldEmail = getUserEmail();
		if (oldEmail != null && oldEmail.equals(email)) {
			return;
		}
		getSession().setAttribute("Email", email);
		if (email == null) {
			getSession().setAttribute("User", null);
		} else {
			User user = DAO.GetUser(email);
			ThreadStorage.setDateTimeZone(user.timeZone);
			getSession().setAttribute("User", user);
		}
	}

	@Override
	void InitialiseProcessing() throws Exception {
		// Are we logged in already?
		if (LoggedIn()) {
			ThreadStorage.setDateTimeZone(GetTimeZone());
		}
	}
}
