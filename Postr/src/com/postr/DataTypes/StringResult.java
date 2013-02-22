package com.postr.DataTypes;

public class StringResult {
		private StringResult(){};
		
		private String result;
		private String errorMessage;
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public String getErrorMessage() {
			return errorMessage;
		}
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
		public static StringResult SuccessResult(String message){
			StringResult result = new StringResult();
			result.setResult(message);
			return result;
		}
		
		public static StringResult ErrorResult(String message){
			StringResult result = new StringResult();
			result.setErrorMessage(message);
			return result;
		}
		

}
