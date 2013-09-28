package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.postr.Result;
import com.postr.DataTypes.BaseSaveable;

@EntitySubclass(index=true)
public abstract class BasePost extends BaseSaveable {
		abstract public void MakePost();
		
		protected BasePost(){}
		
		protected BasePost(long output) {
			this.output = output;
		}
		
		public BasePost(BasePost originalPost) {
			super(originalPost);
			this.output = originalPost.output;
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

		private long output;
		
		private Result result;
		
}
