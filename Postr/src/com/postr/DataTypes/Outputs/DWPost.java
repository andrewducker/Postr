package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.postr.DAO;
import com.postr.MessageLogger;
import com.postr.Result;
import com.postr.Translators.DWTranslator;

@EntitySubclass(index=true)
public class DWPost extends LJPost {

	@Override
	public void MakePost() {
		DWData dwData;
		try {
			dwData = DAO.LoadThing(DWData.class, output, getParent());
		} catch (Exception e) {
			MessageLogger.Severe(this,e.getMessage());
			result = Result.Failure("Failed to load Output data");
			return;
		}
		
		if (tags == null) {
			tags = "";
		}
		
		DWTranslator translator = new DWTranslator();
		result = translator.MakePost(dwData, contents, subject, timeZone, tags.split(","), visibility, autoFormat);
	}
}
