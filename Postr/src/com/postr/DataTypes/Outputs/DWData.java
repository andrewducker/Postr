package com.postr.DataTypes.Outputs;

import java.util.TimeZone;

import com.googlecode.objectify.annotation.EntitySubclass;

@EntitySubclass(index=true)
public class DWData extends LJData {

	public DWData(String userName, String password, TimeZone timeZone) throws Exception {
		super(userName, password, timeZone);
	}

	@Override
	public String getSiteName() {
		return "Dreamwidth";
	};
	
	@SuppressWarnings("unused")
	private DWData(){}

	public DWData(DWData existingDWData, String password, TimeZone timeZone) throws Exception {
		super(existingDWData,password,timeZone);
	}

}