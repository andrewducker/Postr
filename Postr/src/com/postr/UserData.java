package com.postr;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.postr.DataTypes.*;
import com.postr.DataTypes.Inputs.BaseInput;
import com.postr.DataTypes.Outputs.BaseOutput;

public class UserData {
	
	String persona; 
	
	public String getPersona() {
		return persona;
	}

	public void setSession(HttpSession session){
		Long userID = (long)session.getAttribute("UserID");
		persona = (String)session.getAttribute("Persona");
		
		List<BaseSaveable> saveables = DAO.LoadThings(BaseSaveable.class, userID);
		for (BaseSaveable baseSaveable : saveables) {
			if (baseSaveable instanceof BaseOutput) {
				outputs.add((BaseOutput) baseSaveable);
			}
			if (baseSaveable instanceof BaseInput) {
				inputs.add((BaseInput) baseSaveable);
			}
		}
	}
	
	private Vector<BaseInput> inputs = new Vector<BaseInput>();
	
	public List<BaseInput> getInputs(){
		return inputs;
	}

	private Vector<BaseOutput> outputs = new Vector<BaseOutput>();
	
	public List<BaseOutput> getOutputs(){
		return outputs;
	}
}