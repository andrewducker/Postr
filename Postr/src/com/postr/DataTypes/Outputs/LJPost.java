package com.postr.DataTypes.Outputs;

import com.postr.DAO;
import com.postr.DataTypes.Json;
import com.postr.Translators.LJTranslator;

public class LJPost extends BasePost {

	private String subject;
	private String contents;
	private String[] tags;
	
	public LJPost(String subject, String contents, String tags, long output){
		this.subject = subject;
		this.contents = contents;
		this.tags = tags.split(",");
		this.output = output;
	}
	
	
	@Override
	
	public Json MakePost() throws Exception {
		LJData ljData = DAO.LoadThing(LJData.class, output, getParent());
		
		LJTranslator translator = new LJTranslator();
		return translator.MakePost(ljData, contents, subject, tags);
		
	}

}
