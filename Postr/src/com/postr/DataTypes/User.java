package com.postr.DataTypes;

import com.googlecode.objectify.annotation.EntitySubclass;

@EntitySubclass(index=true)
public class User extends BaseSaveable {

	public User(long id){
		this.id = id;
	}
	
	public User(){}
	
	
}
