package com.postr;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.LJData;
import com.postr.DataTypes.Outputs.LJPost;
import com.postr.Translators.LJTranslator;

@SuppressWarnings("serial")
public class LivejournalServlet extends BaseOutputServlet {

	@Override
	protected Result VerifyPassword(String parameters) throws Exception {
		LJData data = Json.FromJson(parameters,LJData.class);
		LJTranslator writer = new LJTranslator();
		return writer.Login(data.userName, data.password);
	}

	@Override
	protected Result SaveData(String parameters) throws Exception {
		LJData ljData = Json.FromJson(parameters,LJData.class);
		ljData.EncryptPassword();
		
		Key<LJData> result = DAO.SaveThing(ljData,GetUserID());
		return new Result("Saved!",result.getId());
	}

	@Override
	protected LJPost CreatePost(String parameters, long userID) {
		LJPost post = Json.FromJson(parameters,LJPost.class);
		post.setParent(userID);
		return post;
	}
	
	@Override
	protected Result SavePost(String parameters) throws Exception {
		LJPost post = CreatePost(parameters,GetUserID());
	
		Key<LJPost> result = DAO.SaveThing(post, GetUserID());
		
		return new Result("Saved!",result.getId());
	}

	
	@Override
	protected Result UpdateData(String parameters) throws Exception {
		LJData newData = Json.FromJson(parameters,LJData.class);
		LJData existingLJData = DAO.LoadThing(LJData.class, newData.getId(), GetUserID());
		
		
		existingLJData.password = newData.password;
		existingLJData.EncryptPassword();
		existingLJData.timeZone = newData.timeZone;
		
		Key<LJData> result = DAO.SaveThing(existingLJData,GetUserID());
		return new Result("Saved!",result.getId());
	}
}