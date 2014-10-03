package com.postr.DataTypes.Outputs;

import java.util.List;

import org.joda.time.DateTime;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.DataTypes.BaseSaveable;

@Subclass(index=true)
public class Feed extends BaseSaveable {
	public List<Long> inputs;
	public long output;
	public int daysToInclude = 1;
	public DateTime nextPost;
	public boolean active = true;
}