package com.postr;

import java.util.List;
import java.util.Vector;

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
		
		List<BaseSaveable> saveables = DAO.LoadThings(BaseSaveable.class, persona);
		for (BaseSaveable baseSaveable : saveables) {
			if (baseSaveable instanceof BaseOutput) {
				outputs.add((BaseOutput) baseSaveable);
			}
		}
	}
	
	private Vector<BaseOutput> outputs = new Vector<BaseOutput>();
	
	public List<BaseOutput> getOutputs(){
		return outputs;
	}
}
