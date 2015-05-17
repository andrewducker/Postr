package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.DAO;
import com.postr.MessageLogger;
import com.postr.Result;
import com.postr.Translators.MetaWebLogTranslator;

@SuppressWarnings("serial")
@Subclass(index=true)
public class WordPressPost extends BasePost {

	String tags;

	
	@Override
	public void MakePost() throws Exception {
		WordPressData data;
		try {
			data = DAO.LoadThing(WordPressData.class, output, getParent());
		} catch (Exception e) {
			MessageLogger.Severe(this,e.getMessage());
			result = Result.Failure("Failed to load Output.");
			return;
		}
		
		MetaWebLogTranslator translator = new MetaWebLogTranslator(data.url);
		result = translator.MakePost(data, contents, subject, tags.split(","));
	}
}