package com.postr.DataTypes.Outputs;

import com.postr.DataTypes.BaseSaveable;
import com.postr.DataTypes.Json;

public abstract class BasePost extends BaseSaveable {
		abstract public Json MakePost() throws Exception;
		
		public long getOutput() {
			return output;
		}

		public void setOutput(long output) {
			this.output = output;
		}

		protected long output;
}
