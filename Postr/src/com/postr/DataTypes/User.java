package com.postr.DataTypes;

import com.googlecode.objectify.annotation.Subclass;

@SuppressWarnings("serial")
@Subclass(index=true)
public class User extends BaseSaveable implements java.io.Serializable{
	public String timeZone = "Europe/London";
}
