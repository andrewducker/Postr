package com.postr.DataTypes.Outputs;

import org.joda.time.DateTimeZone;

import com.googlecode.objectify.annotation.EntitySubclass;

@EntitySubclass(index=true)
public class DWData extends LJData {

	public DWData(String userName, String password, DateTimeZone timeZone) throws Exception {
		super(userName, password, timeZone);
	}

	@Override
	public String getSiteName() {
		return "Dreamwidth";
	};
	
	@SuppressWarnings("unused")
	private DWData(){}

	public DWData(DWData existingDWData, String password, DateTimeZone timeZone) throws Exception {
		super(existingDWData,password,timeZone);
	}
	
}
