package com.postr;

import com.postr.DataTypes.ThreadStorage;
import com.postr.DataTypes.User;

@SuppressWarnings("serial")
abstract class BasePersonaSessionServlet extends BaseJSONServlet {


	protected String GetPersona()
	{
		return (String) session.getAttribute("Persona");
	}
	
	protected boolean LoggedIn(){
		return session.getAttribute("User") != null;
	}
	
	
	protected Long GetUserID()
	{
		return ((User) session.getAttribute("User")).getId();
	}
	
	protected String GetTimeZone()
	{
		return ((User) session.getAttribute("User")).timeZone;
	}
	
	protected void SetTimeZone(String timeZone){
		((User) session.getAttribute("User")).timeZone = timeZone;
	}
	
	protected void SaveUserData(){
		DAO.SaveUser(((User) session.getAttribute("User")));
	}

	protected void SetPersona(String persona) throws Exception
	{
		session.setAttribute("Persona", persona);
		if (persona == null) {
			session.setAttribute("User",null);
		}else{
			User user = DAO.GetUser(persona);
			session.setAttribute("User",user);
		}
	}
	
	@Override
	void InitialiseProcessing(){
		if (LoggedIn()) {
			ThreadStorage.setDateTimeZone(GetTimeZone());
		}
	}
}
