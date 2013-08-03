package com.postr.DataTypes.Outputs;

import com.postr.DAO;
import com.postr.DataTypes.Json;
import com.postr.Translators.DWTranslator;

public class DWPost extends LJPost {

	public DWPost(String subject, String contents, String tags, long output){
		super(subject,contents,tags,output);
	}
	
	public DWPost(DWPost originalPost, String subject, String contents, String tags){
		super(originalPost, subject,contents,tags);
	}
	
	@SuppressWarnings("unused")
	private DWPost(){}
	
	
	@Override
	
	public Json MakePost() throws Exception {
		DWData dwData = DAO.LoadThing(DWData.class, getOutput(), getParent());
		
		DWTranslator translator = new DWTranslator();
		return translator.MakePost(dwData, getContents(), getSubject(), getTags());
		
	}

}
