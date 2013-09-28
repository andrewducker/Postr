package com.postr;

import com.googlecode.objectify.annotation.Embed;

@Embed
public class Result {
	
	@SuppressWarnings("unused")
	private Result(){}
	
	protected Result(boolean failure, String message){
		this.failure = failure;
		this.message = message;
	}
	
	private boolean failure;
	
	private String message;
	
	public boolean isFailure() {
		return failure;
	}

	public String getMessage() {
		return message;
	}

	public static Result Success(String message){
		return new Result(false,message);
	}

	public static Result Failure(String message){
		return new Result(true,message);
	}
	
}
