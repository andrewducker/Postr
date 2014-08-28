package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.DAO;
import com.postr.LivejournalVisibilityTypes;
import com.postr.MessageLogger;
import com.postr.Result;
import com.postr.Translators.LJTranslator;

@Subclass(index=true)
public class LJPost extends BasePost {

	String tags;
	LivejournalVisibilityTypes visibility;
	Boolean autoFormat;
	public String timeZone;

	
	@Override
	public void MakePost() {
		LJData ljData;
		try {
			ljData = DAO.LoadThing(LJData.class, output, getParent());
		} catch (Exception e) {
			MessageLogger.Severe(this,e.getMessage());
			result = Result.Failure("Failed to load Output.");
			return;
		}
		
		if (tags == null) {
			tags = "";
		}
		
		LJTranslator translator = new LJTranslator();
		result = translator.MakePost(ljData, contents, subject, timeZone, tags.split(","),visibility, autoFormat);
	}
}
