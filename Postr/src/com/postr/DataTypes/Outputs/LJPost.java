package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.postr.DAO;
import com.postr.LivejournalVisibilityTypes;
import com.postr.MessageLogger;
import com.postr.Result;
import com.postr.Translators.LJTranslator;

@EntitySubclass(index=true)
public class LJPost extends BasePost {

	private String tags;
	private LivejournalVisibilityTypes visibility;
	
	protected String getTags() {
		return tags;
	}

	public LJPost(String subject, String contents, String tags, LivejournalVisibilityTypes visibility, long output){
		super(output, subject, contents);
		this.tags = tags;
		this.visibility = visibility;
	}

	protected LivejournalVisibilityTypes getVisibility() {
		return visibility;
	}

	protected LJPost(){}

	@Override
	public void MakePost() {
		LJData ljData;
		try {
			ljData = DAO.LoadThing(LJData.class, getOutput(), getParent());
		} catch (Exception e) {
			MessageLogger.Severe(this,e.getMessage());
			setResult(Result.Failure("Failed to load Output."));
			return;
		}
		
		LJTranslator translator = new LJTranslator();
		setResult(translator.MakePost(ljData, getContents(), getSubject(), tags.split(","),visibility));
	}
}
