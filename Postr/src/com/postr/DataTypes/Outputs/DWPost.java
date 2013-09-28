package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.postr.DAO;
import com.postr.MessageLogger;
import com.postr.Result;
import com.postr.Translators.DWTranslator;

@EntitySubclass(index=true)
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
	public void MakePost() {
		DWData dwData;
		try {
			dwData = DAO.LoadThing(DWData.class, getOutput(), getParent());
		} catch (Exception e) {
			MessageLogger.Severe(this,e.getMessage());
			setResult(Result.Failure("Failed to load Output data"));
			return;
		}
		
		DWTranslator translator = new DWTranslator();
		setResult(translator.MakePost(dwData, getContents(), getSubject(), getTags()));
	}

}
