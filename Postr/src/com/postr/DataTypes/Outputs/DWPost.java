package com.postr.DataTypes.Outputs;

import com.postr.DAO;
import com.postr.DataTypes.Json;
import com.postr.Translators.DWTranslator;

public class DWPost extends BasePost {

	private String subject;
	private String contents;
	private String[] tags;
	
	public DWPost(String subject, String contents, String tags, long output){
		this.subject = subject;
		this.contents = contents;
		this.tags = tags.split(",");
		this.output = output;
	}
	
	
	@Override
	
	public Json MakePost() throws Exception {
		DWData dwData = DAO.LoadThing(DWData.class, output, getParent());
		
		DWTranslator translator = new DWTranslator();
		return translator.MakePost(dwData, contents, subject, tags);
		
	}

}
