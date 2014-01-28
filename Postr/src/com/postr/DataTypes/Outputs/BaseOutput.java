package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.postr.DataTypes.BaseSaveable;

@EntitySubclass(index=true)
public abstract class BaseOutput extends BaseSaveable {

	public BaseOutput(){this.siteName = getSiteName();}
	
	public String userName;
	public String password;
	

	protected abstract String getSiteName();
	
	//User for JSon
	@SuppressWarnings("unused")
	private String siteName;
	
}