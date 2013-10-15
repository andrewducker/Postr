package com.postr;

import com.googlecode.objectify.annotation.Embed;

@Embed
public class Result {
	
	@SuppressWarnings("unused")
	private Result(){}
	
	private Result(boolean failure, String message){
		this.failure = failure;
		this.message = message;
	}
	
	protected Result(String message, long id){
		this.id = id;
		this.message = message;
	}
	
	public long id; // NO_UCD (JSON)
	
	boolean failure;
	
	String message;
	
	long postingTime; // NO_UCD (JSON�)
	
	public static Result Success(String message){
		return new Result(false,message);
	}

	public static Result Failure(String message){
		return new Result(true,message);
	}
	
}
