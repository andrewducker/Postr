package com.postr;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.LJData;
import com.postr.DataTypes.Outputs.LJPost;
import com.postr.Translators.LJTranslator;

@SuppressWarnings("serial")
public class LivejournalServlet extends BaseOutputServlet {

	@Override
	protected Json VerifyPassword(Json parameters) throws Exception {
		LJData data = parameters.FromJson(LJData.class);
		LJTranslator writer = new LJTranslator();
		return writer.Login(data.userName, data.password);
	}

	@Override
	protected Json SaveData(Json parameters) throws Exception {
		LJData ljData = parameters.FromJson(LJData.class);
		ljData.EncryptPassword();
		
		Key<LJData> result = DAO.SaveThing(ljData,GetUserID());
		Json toReturn = Json.SuccessResult("Saved!");
		toReturn.setData("id", result.getId());
		return toReturn;
	}

	@Override
	protected LJPost CreatePost(Json parameters, long userID) {
		LJPost post = parameters.FromJson(LJPost.class);
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
	protected Json UpdateData(Json parameters) throws Exception {
		Long key = parameters.getLong("key");
		LJData existingLJData = DAO.LoadThing(LJData.class, key, GetUserID());
		
		LJData newData = parameters.FromJson(LJData.class);
		
		existingLJData.password = newData.password;
		existingLJData.EncryptPassword();
		existingLJData.timeZone = newData.timeZone;
		
		Key<LJData> result = DAO.SaveThing(existingLJData,GetUserID());
		Json toReturn =Json.SuccessResult("Saved!"); 
		toReturn.setData("id", result.getId());
		return toReturn;
	}
}