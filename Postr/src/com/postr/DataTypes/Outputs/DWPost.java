package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.DAO;
import com.postr.LogHandler;
import com.postr.Result;
import com.postr.Translators.DWTranslator;

@SuppressWarnings("serial")
@Subclass(index=true)
public class DWPost extends LJPost {

	@Override
	public void MakePost() {
		DWData dwData;
		try {
			dwData = DAO.LoadThing(DWData.class, output, getParent());
		} catch (Exception e) {
			LogHandler.logSevere(this,e.getMessage());
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
