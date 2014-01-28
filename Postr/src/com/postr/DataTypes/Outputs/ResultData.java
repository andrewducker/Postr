package com.postr.DataTypes.Outputs;

public class ResultData {
	long id;
	ResultStateEnum state;
	public ResultData(long id, ResultStateEnum state){
		this.id = id;
		this.state = state;
	}
}
