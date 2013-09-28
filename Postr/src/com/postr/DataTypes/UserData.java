package com.postr.DataTypes;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.joda.time.DateTimeZone;

import com.google.gson.GsonBuilder;
import com.postr.DAO;
import com.postr.DataTypes.Inputs.BaseInput;
import com.postr.DataTypes.Outputs.BaseOutput;
import com.postr.DataTypes.Outputs.BasePost;

public class UserData {

	static GsonBuilder builder;
	
	static	{
		builder = new GsonBuilder();
		builder.registerTypeAdapter(DateTimeZone.class, new DateTimeZoneJsonAdapter());
	}
	
	public UserData(String persona, Long userID){
		this.persona = persona;
		List<BaseSaveable> saveables = DAO.LoadThings(BaseSaveable.class, userID);
		for (BaseSaveable baseSaveable : saveables) {
			if (baseSaveable instanceof BaseOutput) {
				outputs.add((BaseOutput) baseSaveable);
			}
			if (baseSaveable instanceof BaseInput) {
				inputs.add((BaseInput) baseSaveable);
			}
			if (baseSaveable instanceof BasePost) {
				posts.add((BasePost) baseSaveable);
			}

		}
	}
	
	public String AsJson(){
		return builder.create().toJson(this);
	}
	
	public String persona;
	
	public Collection<BaseInput> inputs = new Vector<BaseInput>();
	
	public Collection<BaseOutput> outputs = new Vector<BaseOutput>();
	
	public Collection<BasePost> posts = new Vector<BasePost>();
}
