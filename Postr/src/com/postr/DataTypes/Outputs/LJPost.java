package com.postr.DataTypes.Outputs;

import com.postr.DAO;
import com.postr.DataTypes.Json;
import com.postr.Translators.LJTranslator;

public class LJPost extends BasePost {

	private String subject;
	private String contents;
	private String[] tags;
	
	protected String getSubject() {
		return subject;
	}

	protected String getContents() {
		return contents;
	}

	protected String[] getTags() {
		return tags;
	}

	public LJPost(String subject, String contents, String tags, long output){
		super(output);
		this.subject = subject;
		this.contents = contents;
		this.tags = tags.split(",");
	}

	protected LJPost(){}
	
	public LJPost(LJPost originalPost, String subject, String contents,
			String tags) {
		super(originalPost);
		this.subject = subject;
		this.contents = contents;
		this.tags = tags.split(",");
	}


	@Override
	public Json MakePost() throws Exception {
		LJData ljData = DAO.LoadThing(LJData.class, getOutput(), getParent());
		
		LJTranslator translator = new LJTranslator();
		return translator.MakePost(ljData, contents, subject, tags);
		
	}

}
