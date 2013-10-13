package com.postr.DataTypes.Outputs;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.postr.Result;
import com.postr.DataTypes.BaseSaveable;

@EntitySubclass(index=true)
public abstract class BasePost extends BaseSaveable {
		abstract public void MakePost();
		
		public String subject;
		public String contents;

		
		public void setPostingTime(){
			postingTime = DateTime.now(DateTimeZone.UTC);
		}
		
		public DateTime getPostingTime(){
			return postingTime;
		}

		public long output;
		
		public Result result;
		
		private DateTime postingTime;
}
