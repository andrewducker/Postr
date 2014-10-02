package com.postr.DataTypes.Outputs;

import java.util.List;

import org.joda.time.DateTime;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.DataTypes.BaseSaveable;
import com.postr.DataTypes.Inputs.BaseInput;

@Subclass(index=true)
public class Feed extends BaseSaveable {
	public List<BaseInput> inputs;
	public BaseOutput output;
	public int daysToInclude = 1;
	public DateTime nextPost;
	public boolean active;
}