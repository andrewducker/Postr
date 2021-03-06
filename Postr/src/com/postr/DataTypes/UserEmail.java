package com.postr.DataTypes;

import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;

@SuppressWarnings("serial")
@Subclass(index=true)
public class UserEmail extends BaseSaveable {
	public @Index String email; // NO_UCD (Data)
}