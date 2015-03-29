package com.postr;

import javax.servlet.http.Cookie;

import com.postr.DataTypes.ThreadStorage;
import com.postr.DataTypes.User;

@SuppressWarnings("serial")
abstract class BasePersonaSessionServlet extends BaseJSONServlet {

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
			Cookie removal = new Cookie("PostrID", "");
			removal.setMaxAge(0);
			AddCookie(removal);
		} else {
			User user = DAO.GetUser(persona);
			Cookie idCookie = CookieHandler.GenerateCookie(persona);
			AddCookie(idCookie);
			ThreadStorage.setDateTimeZone(user.timeZone);
			getSession().setAttribute("User", user);
		}
	}

	@Override
	void InitialiseProcessing() throws Exception {
		// Are we logged in already?
		if (LoggedIn()) {
			ThreadStorage.setDateTimeZone(GetTimeZone());
		} else {
			// Is there an ID cookie set?
			Cookie[] cookies = GetCookies();
			Cookie idCookie = null;
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("PostrID")) {
						idCookie = cookie;
						break;
					}
				}
			}
			if (idCookie != null) {
				String email = CookieHandler.ParseCookie(idCookie.getValue());
				if (email != null) {
					SetPersona(email);
				}
			}
		}
	}
}
