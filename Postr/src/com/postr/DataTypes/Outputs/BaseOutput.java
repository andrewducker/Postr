package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.postr.DataTypes.BaseSaveable;

@EntitySubclass(index=true)
public abstract class BaseOutput extends BaseSaveable {

	protected BaseOutput(){this.siteName = getSiteName();}
	
	protected BaseOutput(String userName, String password) throws Exception{
		this.userName = userName;
		this.password = password;
		siteName = getSiteName();
	}
	
	public BaseOutput(BaseOutput existingData, String password) {
		super(existingData);
		this.userName = existingData.userName;
		this.password = password;
		siteName = getSiteName();
	}

	protected String userName;
	private transient String password;

	
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
	
	public abstract String getSiteName();
	
	//User for JSon
	@SuppressWarnings("unused")
	private String siteName;
	
}