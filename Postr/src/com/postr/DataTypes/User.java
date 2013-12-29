package com.postr.DataTypes;

import com.googlecode.objectify.annotation.EntitySubclass;

@SuppressWarnings("serial")
@EntitySubclass(index=true)
public class User extends BaseSaveable implements java.io.Serializable{
	public String timeZone = "Europe/London";
}
