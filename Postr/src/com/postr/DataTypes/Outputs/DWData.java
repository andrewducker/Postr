package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.Subclass;

@Subclass(index=true)
public class DWData extends LJData {

	@Override
	public String getSiteName() {
		return "Dreamwidth";
	};
}
