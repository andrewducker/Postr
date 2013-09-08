package com.postr;

import java.util.Collection;
import javax.servlet.http.HttpSession;

import com.postr.DataTypes.Inputs.BaseInput;
import com.postr.DataTypes.Outputs.BaseOutput;

public class UserData {
	
	HttpSession session;
	
	public void setSession(HttpSession session){
		this.session = session;
		SetUp();
	}
	
	private void SetUp(){
		Long userID = (long)session.getAttribute("UserID");
		String persona = (String)session.getAttribute("Persona");

		userData = new com.postr.DataTypes.UserData(persona, userID);
	}
	
	private com.postr.DataTypes.UserData userData;
	
	public Collection<BaseInput> getInputs(){
		return userData.inputs;
	}

	public Collection<BaseOutput> getOutputs(){
		return userData.outputs;
	}
	
	public String getPersona() {
		return userData.persona;
	}


}