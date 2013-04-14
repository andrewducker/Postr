package com.postr;

import java.util.List;
import javax.servlet.http.HttpSession;

import com.postr.DataTypes.*;

public class UserData {
	public String getPersona(){
		return persona;
	}
	
	private String persona;
	
	public void setSession(HttpSession session){
		persona = (String)session.getAttribute("Persona");
		if (persona == null) {
			persona = "Not logged in";
		}
	}
	
	public List<BaseOutput> getOutputs(){
		List<BaseOutput> outputs = DAO.LoadThings(BaseOutput.class, persona);
		return outputs;
	}
}
