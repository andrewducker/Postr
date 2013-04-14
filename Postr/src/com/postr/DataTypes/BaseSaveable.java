package com.postr.DataTypes;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class BaseSaveable {
	@Id Long id;
	public void setPersona(String persona) {
		this.persona = persona;
	}

	
	@Index
	String persona;
	
	protected BaseSaveable(){}
}
