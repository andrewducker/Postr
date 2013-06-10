package com.postr.DataTypes;

import com.googlecode.objectify.annotation.EntitySubclass;

@EntitySubclass(index=true)
public abstract class BaseOutput extends BaseSaveable {

	protected BaseOutput(){}
	
	protected BaseOutput(String userName, String password) throws Exception{
		this.userName = userName;
		this.password = password;
	}
	
	public BaseOutput(BaseOutput existingData, String password) {
		super(existingData);
		this.userName = existingData.userName;
		this.password = password;
	}

	protected String userName;
	private String password;

	
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
	
	public abstract String getSiteName();
	
}