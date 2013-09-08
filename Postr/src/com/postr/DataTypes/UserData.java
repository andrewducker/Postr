package com.postr.DataTypes;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import com.postr.DAO;
import com.postr.DataTypes.Inputs.BaseInput;
import com.postr.DataTypes.Outputs.BaseOutput;

public class UserData {

	public UserData(String persona, Long userID){

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
	
	public String persona;
	
	public Collection<BaseInput> inputs = new Vector<BaseInput>();
	
	public Collection<BaseOutput> outputs = new Vector<BaseOutput>();
}
