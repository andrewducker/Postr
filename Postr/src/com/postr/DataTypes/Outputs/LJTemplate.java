package com.postr.DataTypes.Outputs;

import java.util.List;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.DataTypes.Inputs.BaseInput;

@Subclass(index=true)
public class LJTemplate extends LJPost {
	public List<BaseInput> inputs;
}
