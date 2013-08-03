package com.postr.DataTypes.Outputs;

import com.postr.DataTypes.BaseSaveable;
import com.postr.DataTypes.Json;

public abstract class BasePost extends BaseSaveable {
		abstract public Json MakePost() throws Exception;
		
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

		private long output;
}
