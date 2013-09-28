package com.postr.DataTypes.Outputs;

import org.joda.time.DateTimeZone;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.postr.DataTypes.PasswordEncryptor;

@EntitySubclass(index=true)
public class LJData extends BaseOutput{
	private DateTimeZone timeZone;

	public DateTimeZone getTimeZone() {
		return timeZone;
	}
	
	public LJData(String userName, String password, DateTimeZone timeZone) throws Exception{
		super(userName,PasswordEncryptor.MD5Hex(password));
		this.timeZone = timeZone;
	}
	
	protected LJData(){}
	
	public LJData(LJData existingLJData, String password, DateTimeZone timeZone) throws Exception {
		super(existingLJData,PasswordEncryptor.MD5Hex(password));
		this.timeZone = timeZone;
	}

	@Override
	public String getSiteName() {
		return "Livejournal";
	}
}
