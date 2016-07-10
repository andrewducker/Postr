	package com.postr;

import com.postr.DataTypes.ThreadStorage;
import com.postr.DataTypes.User;

@SuppressWarnings("serial")
abstract class BasePersonaSessionServlet extends BaseServlet {

	protected String GetPersona() {
		return (String) getSession().getAttribute("Persona");
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

	protected void SetPersona(String persona) throws Exception {
		String oldPersona = GetPersona();
		if (oldPersona != null && oldPersona.equals(persona)) {
			return;
		}
		getSession().setAttribute("Persona", persona);
		if (persona == null) {
			getSession().setAttribute("User", null);
		} else {
			User user = DAO.GetUser(persona);
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
