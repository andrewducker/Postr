package com.postr.DataTypes;

import java.util.List;
import java.util.Vector;

import com.postr.DAO;
import com.postr.DataTypes.Inputs.BaseInput;
import com.postr.DataTypes.Outputs.BaseOutput;
import com.postr.DataTypes.Outputs.BasePost;
import com.postr.DataTypes.Outputs.Feed;


public class UserData {

	public UserData(String persona, Long userID) throws Exception{
		this.persona = persona;
		List<BaseSaveable> saveables = DAO.LoadThings(BaseSaveable.class, userID);
		for (BaseSaveable baseSaveable : saveables) {
			if (baseSaveable instanceof Feed) {
				feeds.add((Feed) baseSaveable);
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
		}
		
		timeZone = DAO.LoadThing(User.class, userID, userID).timeZone;
		
		possibleOutputs.add("Dreamwidth");
		possibleOutputs.add("Livejournal");
		possibleOutputs.add("TestOutput");
		
		possibleInputs.add("Delicious");
		possibleInputs.add("Pinboard");
	}
	
	@SuppressWarnings("unused")
	private String persona; // NO_UCD (JSON)
	
	@SuppressWarnings("unused")
	private String timeZone;
	
	private List<BaseInput> inputs = new Vector<BaseInput>();
	
	private List<BaseOutput> outputs = new Vector<BaseOutput>();
	
	private List<BasePost> posts = new Vector<BasePost>();
	
	private List<Feed> feeds = new Vector<Feed>();
	
	private List<String> possibleOutputs = new Vector<String>();
	
	private List<String> possibleInputs = new Vector<String>();
	
}