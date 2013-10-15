package com.postr.DataTypes.Outputs;

import org.joda.time.DateTime;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.postr.Result;
import com.postr.DataTypes.BaseSaveable;

@EntitySubclass(index=true)
public abstract class BasePost extends BaseSaveable {
		abstract public void MakePost();
		
		String subject;
		String contents;
		long output;
		public Result result;
		public DateTime postingTime;
}
