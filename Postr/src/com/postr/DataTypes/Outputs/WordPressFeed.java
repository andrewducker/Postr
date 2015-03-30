package com.postr.DataTypes.Outputs;

import java.util.List;

import com.googlecode.objectify.annotation.Subclass;

@SuppressWarnings("serial")
@Subclass(index = true)
public class WordPressFeed extends BaseFeed {

	String tags;
	@Override
	BasePost generatePost(String postSubject, String postContents,
			List<String> tags) throws Exception {
		WordPressPost post = new WordPressPost();
		post.contents = postContents;
		post.siteName = "Livejournal";
		post.subject = postSubject;
		post.output = output;
		
	    String tagsToUse = "";
	    for (String tag : tags) {
	    	if(tagsToUse.length()>0)
	    	{
	    		tagsToUse+=",";
	    	}
			tagsToUse +=tag; 
		}
	    post.tags = tagsToUse;
		return post;
	}
}
