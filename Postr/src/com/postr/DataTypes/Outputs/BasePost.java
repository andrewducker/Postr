package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.postr.Result;
import com.postr.DataTypes.BaseSaveable;

@EntitySubclass(index=true)
public abstract class BasePost extends BaseSaveable {
		abstract public void MakePost();
		
		protected BasePost(){this.siteName = getSiteName();}
		
		protected BasePost(long output) {
			this.output = output;
			this.siteName = getSiteName();
		}
		
		public BasePost(BasePost originalPost) {
			super(originalPost);
			this.output = originalPost.output;
			this.siteName = getSiteName();
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
		
		//User for JSon
		@SuppressWarnings("unused")
		private String siteName;
		protected abstract String getSiteName();		
}
