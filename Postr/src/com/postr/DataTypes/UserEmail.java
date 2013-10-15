package com.postr.DataTypes;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.googlecode.objectify.annotation.Index;

@EntitySubclass(index=true)
public class UserEmail extends BaseSaveable {
	public @Index String email; // NO_UCD (Data)
}