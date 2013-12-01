package com.postr;

@SuppressWarnings("serial")
abstract class BasePersonaSessionServlet extends BaseJSONServlet {


	protected String GetPersona()
	{
		return (String) session.getAttribute("Persona");
	}
	
	protected boolean LoggedIn(){
		return session.getAttribute("UserID") != null;
	}
	
	
	protected Long GetUserID()
	{
		return (Long) session.getAttribute("UserID");
	}

	protected void SetPersona(String persona) throws Exception
	{
		session.setAttribute("Persona", persona);
		if (persona == null) {
			session.setAttribute("UserID",null);
		}else{
			Long userID = DAO.GetUserID(persona);
			session.setAttribute("UserID",userID);
		}
	}
}
