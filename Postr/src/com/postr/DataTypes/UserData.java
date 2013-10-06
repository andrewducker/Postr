package com.postr.DataTypes;

import java.util.List;
import java.util.Vector;

import com.postr.DAO;
import com.postr.DataTypes.Inputs.BaseInput;
import com.postr.DataTypes.Outputs.BaseOutput;
import com.postr.DataTypes.Outputs.BasePost;
import com.postr.DataTypes.Outputs.BasePostComparator;


public class UserData {

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
		
		java.util.Collections.sort(posts, new BasePostComparator());
	}
	
	public String persona;
	

	public List<BaseInput> inputs = new Vector<BaseInput>();
	
	public List<BaseOutput> outputs = new Vector<BaseOutput>();
	
	public List<BasePost> posts = new Vector<BasePost>();
}
