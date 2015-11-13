package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.DAO;
import com.postr.LivejournalVisibilityTypes;
import com.postr.LogHandler;
import com.postr.Result;

@SuppressWarnings("serial")
@Subclass(index=true)
public class TestPost extends BasePost {

	String tags;
	LivejournalVisibilityTypes visibility;
	Boolean autoFormat;
	public String timeZone;

	
	@Override
	public void MakePost() {
		//Not used as test data.
		@SuppressWarnings("unused")
		TestData ljData;
		try {
			ljData = DAO.LoadThing(TestData.class, output, getParent());
		} catch (Exception e) {
			LogHandler.logSevere(this,e.getMessage());
			result = Result.Failure("Failed to load Output.");
			return;
		}
		result = Result.Success("Test data, successfully not posted");
		return;
		}
}
