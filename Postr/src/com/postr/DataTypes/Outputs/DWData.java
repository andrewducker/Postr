package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.Subclass;

@SuppressWarnings("serial")
@Subclass(index=true)
public class DWData extends LJData {

	@Override
	public String getSiteName() {
		return "Dreamwidth";
	};
}
