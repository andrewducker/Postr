package com.postr;

import org.joda.time.DateTimeZone;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.LJData;
import com.postr.DataTypes.Outputs.LJPost;
import com.postr.Translators.LJTranslator;

@SuppressWarnings("serial")
public class LivejournalServlet extends BaseOutputServlet {

	@Override
	protected Json VerifyPassword(Json parameters) throws Exception {
		String userName = parameters.getString("userName");
		String password = parameters.getString("password");
		LJTranslator writer = new LJTranslator();
		return writer.Login(userName, password);
	}

	@Override
	protected Json SaveData(Json parameters) throws Exception {
		String userName = parameters.getString("userName");
		String password = parameters.getString("password");
		String timeZone = parameters.getString("timeZone");
		LJData ljData = new LJData(
				userName, password,
				DateTimeZone.forID(timeZone));
		
		Key<LJData> result = DAO.SaveThing(ljData,GetUserID());
		Json toReturn = Json.SuccessResult("Saved!");
		toReturn.setData("id", result.getId());
		return toReturn;
	}

	@Override
	protected LJPost CreatePost(Json parameters, long userID) {
		String contents = parameters.getString("contents");
		String subject = parameters.getString("subject");
		String tags = parameters.getString("tags");
		long output = parameters.getLong("output");
		LivejournalVisibilityTypes visibility =  LivejournalVisibilityTypes.valueOf(parameters.getString("visibility"));
		LJPost post = new LJPost(subject, contents, tags,visibility, output);
		post.setParent(userID);
		return post;
	}
	
	@Override
	protected Json SavePost(Json parameters) throws Exception {
		LJPost post = CreatePost(parameters,GetUserID());
	
		Key<LJPost> result = DAO.SaveThing(post, GetUserID());
		
		Json toReturn = Json.SuccessResult("Saved!");
		toReturn.setData("id", result.getId());
		return toReturn;
	}

	
	@Override
	protected Json UpdatePost(Json parameters) throws Exception {
		String contents = parameters.getString("contents");
		String subject = parameters.getString("subject");
		String tags = parameters.getString("tags");
		Long key = parameters.getLong("id");
		
		LJPost post = DAO.LoadThing(LJPost.class, key, GetUserID());
		
		LJPost newPost = new LJPost(post, subject, contents, tags);
		
		DAO.SaveThing(newPost, GetUserID());
		
		return Json.SuccessResult("Saved!");
	}
	
	@Override
	protected Json UpdateData(Json parameters) throws Exception {
		Long key = parameters.getLong("key");
		String password = parameters.getString("password");
		String timeZone = parameters.getString("timeZone");
		LJData existingLJData = DAO.LoadThing(LJData.class, key, GetUserID());
		
		LJData newDWData = new LJData(existingLJData,password,DateTimeZone.forID(timeZone));
		
		Key<LJData> result = DAO.SaveThing(newDWData,GetUserID());
		Json toReturn =Json.SuccessResult("Saved!"); 
		toReturn.setData("id", result.getId());
		return toReturn;
	}
}