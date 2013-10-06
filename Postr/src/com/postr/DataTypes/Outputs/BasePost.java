package com.postr.DataTypes.Outputs;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.postr.Result;
import com.postr.DataTypes.BaseSaveable;

@EntitySubclass(index=true)
public abstract class BasePost extends BaseSaveable {
		abstract public void MakePost();
		
		protected BasePost(){}
		
		protected BasePost(long output, String subject, String contents) {
			this.output = output;
			this.subject = subject;
			this.contents = contents;
		}
		
		public BasePost(BasePost originalPost) {
			super(originalPost);
			this.subject = originalPost.subject;
			this.contents = originalPost.contents;
			this.output = originalPost.output;
		}
		private String subject;
		private String contents;

		protected String getSubject() {
			return subject;
		}

		protected String getContents() {
			return contents;
		}

		public long getOutput() {
			return output;
		}

		public void setOutput(long output) {
			this.output = output;
		}

		public Result getResult() {
			return result;
		}

		public void setResult(Result result) {
			this.result = result;
		}
		
		public void setPostingTime(){
			postingTime = DateTime.now(DateTimeZone.UTC);
		}
		
		public DateTime getPostingTime(){
			return postingTime;
		}

		private long output;
		
		private Result result;
		
		private DateTime postingTime;
}
