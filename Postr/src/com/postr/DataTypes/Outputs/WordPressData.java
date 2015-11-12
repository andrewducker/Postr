package com.postr.DataTypes.Outputs;
import com.googlecode.objectify.annotation.Subclass;

@SuppressWarnings("serial")
@Subclass(index = true)
public class WordPressData extends BaseOutput {
	@Override
	public String getSiteName() {
		return "WordPress";
	}

	public String blogId;
}
