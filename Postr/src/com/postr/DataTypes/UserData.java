package com.postr.DataTypes;

import java.util.List;
import java.util.Vector;

import com.postr.DAO;
import com.postr.DataTypes.Inputs.BaseInput;
import com.postr.DataTypes.Outputs.BaseOutput;
import com.postr.DataTypes.Outputs.BasePost;
import com.postr.DataTypes.Outputs.ITemplate;


public class UserData {

	public UserData(String persona, Long userID){
		this.persona = persona;
		List<BaseSaveable> saveables = DAO.LoadThings(BaseSaveable.class, userID);
		for (BaseSaveable baseSaveable : saveables) {
			if (baseSaveable instanceof ITemplate) {
				templates.add((ITemplate) baseSaveable);
				continue;
			}
			if (baseSaveable instanceof BaseOutput) {
				BaseOutput output =(BaseOutput) baseSaveable;
				output.password = null;
				outputs.add(output);
				continue;
			}
			if (baseSaveable instanceof BaseInput) {
				inputs.add((BaseInput) baseSaveable);
				continue;
			}
			if (baseSaveable instanceof BasePost) {
				posts.add((BasePost) baseSaveable);
				continue;
			}
			possibleOutputs.add("Dreamwidth");
			possibleOutputs.add("Livejournal");
			
		}
	}
	
	@SuppressWarnings("unused")
	private String persona; // NO_UCD (JSON)
	
	private List<BaseInput> inputs = new Vector<BaseInput>();
	
	private List<BaseOutput> outputs = new Vector<BaseOutput>();
	
	private List<BasePost> posts = new Vector<BasePost>();
	
	private List<ITemplate> templates = new Vector<ITemplate>();
	
	private List<String> possibleOutputs = new Vector<String>();
	
}
