package com.postr.DataTypes.Inputs;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.postr.DataTypes.BaseSaveable;

@EntitySubclass(index=true)
public abstract class BaseInput extends BaseSaveable {
	protected String userName;
	
	protected BaseInput(){this.siteName = getSiteName();}

	public BaseInput(String userName) {
		super();
		this.userName = userName;
		this.siteName = getSiteName();
	}
	
	//Needed for JSon
	@SuppressWarnings("unused")
	private String siteName;

	public abstract String getSiteName();
	
	public String getUserName() {
		return userName;
	}
}
