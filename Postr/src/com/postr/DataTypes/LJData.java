package com.postr.DataTypes;

import java.util.TimeZone;

public class LJData extends BaseSaveable {
	private final String userName;
	private final String password;
	private final TimeZone timeZone;
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

}
