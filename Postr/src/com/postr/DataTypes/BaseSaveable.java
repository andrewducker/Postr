package com.postr.DataTypes;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class BaseSaveable {
	@Id Long id;
	public void setPersona(String persona) {
		this.persona = persona;
	}

	String persona;
	
	protected BaseSaveable(){}
}
