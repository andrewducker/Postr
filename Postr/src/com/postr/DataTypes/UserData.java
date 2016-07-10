package com.postr.DataTypes;

import java.util.List;
import java.util.Vector;

import com.postr.AppData;
import com.postr.DAO;
import com.postr.DataTypes.Inputs.BaseInput;
import com.postr.DataTypes.Outputs.BaseFeed;
import com.postr.DataTypes.Outputs.BaseOutput;
import com.postr.DataTypes.Outputs.BasePost;


public class UserData {

	public UserData(String email, Long userID) throws Exception{
		this.persona = email;
		List<BaseSaveable> saveables = DAO.LoadThings(BaseSaveable.class, userID);
		for (BaseSaveable baseSaveable : saveables) {
			if (baseSaveable instanceof BaseFeed) {
				feeds.add((BaseFeed) baseSaveable);
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
		
		AppData appData = DAO.getAppData();
		
		isAdmin = appData.Administrator.toLowerCase().equalsIgnoreCase(email);
		wordPressClientId = appData.wordPressClientId;
		wordPressClientSecret = appData.wordPressClientSecret;
		
		possibleOutputs.add("Dreamwidth");
		possibleOutputs.add("Livejournal");
		possibleOutputs.add("WordPress");
		possibleOutputs.add("TestOutput");
		
		possibleInputs.add("Delicious");
		possibleInputs.add("Pinboard");
		possibleInputs.add("Pinterest");
		possibleInputs.add("TestInput");
	}
	
	@SuppressWarnings("unused")
	private String persona; // NO_UCD (JSON)
	
	@SuppressWarnings("unused")
	private String timeZone;

	@SuppressWarnings("unused")
	private String wordPressClientId;
	
	@SuppressWarnings("unused")
	private String wordPressClientSecret;

	@SuppressWarnings("unused")
	private boolean isAdmin;

	private List<BaseInput> inputs = new Vector<BaseInput>();
	
	private List<BaseOutput> outputs = new Vector<BaseOutput>();
	
	private List<BasePost> posts = new Vector<BasePost>();
	
	private List<BaseFeed> feeds = new Vector<BaseFeed>();
	
	private List<String> possibleOutputs = new Vector<String>();
	
	private List<String> possibleInputs = new Vector<String>();
}