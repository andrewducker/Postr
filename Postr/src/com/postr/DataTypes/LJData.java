package com.postr.DataTypes;

import java.util.TimeZone;

import com.googlecode.objectify.annotation.EntitySubclass;

@EntitySubclass(index=true)
public class LJData extends BaseOutput {
	protected String userName;
	private String password;
	private TimeZone timeZone;
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
	public TimeZone getTimeZone() {
		return timeZone;
	}
	public LJData(String userName, String password, TimeZone timeZone) throws Exception{
		this.userName = userName;
		this.password = PasswordEncryptor.MD5Hex(password);
		this.timeZone = timeZone;
	}
	
	protected LJData(){}
	
	@Override
	public String getDescription() {
		return userName+"@Livejournal";
	}

}
