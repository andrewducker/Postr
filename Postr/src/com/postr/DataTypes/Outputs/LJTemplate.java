package com.postr.DataTypes.Outputs;

import java.util.List;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.postr.DataTypes.Inputs.BaseInput;

@EntitySubclass(index=true)
public class LJTemplate extends LJPost {
	public List<BaseInput> inputs;
}
