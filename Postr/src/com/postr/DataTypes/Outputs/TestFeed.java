package com.postr.DataTypes.Outputs;

import java.util.List;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.DAO;
import com.postr.LivejournalVisibilityTypes;
import com.postr.DataTypes.User;

@Subclass(index = true)
public class TestFeed extends BaseFeed {

	String tags;
	LivejournalVisibilityTypes visibility;
	Boolean autoFormat;
	public String timeZone;
	
	@Override
	BasePost generatePost(String postSubject, String postContents,
			List<String> tags) throws Exception {
		TestPost post = new TestPost();
		post.autoFormat = autoFormat;
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
	    
	    User user = DAO.LoadThing(User.class, this.getParent());
	    post.timeZone = user.timeZone;
	    		
	    post.visibility = visibility;
		
		return post;
	}
}
