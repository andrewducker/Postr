package com.postr.DataTypes;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;

public class Json  {
		
	public final static String ERROR_MESSAGE = "errorMessage";
	public final static String RESULT = "result";
	
		private Json(){}
		
		@SuppressWarnings("unchecked")
		public Json(String data){
			this.data = (StringMap<Object>)new Gson().fromJson(data, Object.class);
		}
	
		private StringMap<Object> data = new StringMap<Object>();
		
		public void setData(String key, Object object) {
			data.put(key,object);
		}
		
		public String getString(String key){
			return (String) data.get(key);
		}
		
		public static Json SuccessResult(String message){
			Json result = new Json();
			result.data.put(RESULT, message);
			return result;
		}
		
		public static Json ErrorResult(String message){
			Json result = new Json();
			result.data.put(ERROR_MESSAGE, message);
			return result;
		}
		
		public Long getLong(String key){
			Double parameter = (Double)data.get(key); 
			return parameter.longValue();
		}

		
		public String ToJson(){
			return new Gson().toJson(data);
		}
}
