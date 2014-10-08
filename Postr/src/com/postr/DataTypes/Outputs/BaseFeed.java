package com.postr.DataTypes.Outputs;

import java.util.List;

import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;

@Subclass(index=true)
public abstract class BaseFeed extends BasePost {
	public List<Long> inputs;
	public int daysToInclude = 1;
	@Index public boolean active = true;
}