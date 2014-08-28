package com.postr.DataTypes.Inputs;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.DataTypes.BaseSaveable;

@Subclass(index=true)
public abstract class BaseInput extends BaseSaveable {
	private String userName;
	
	protected BaseInput(){this.siteName = getSiteName();}

	BaseInput(String userName) {
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
