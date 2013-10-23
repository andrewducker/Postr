package com.postr;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.LJData;
import com.postr.DataTypes.Outputs.LJPost;
import com.postr.DataTypes.Outputs.LJTemplate;
import com.postr.Translators.LJTranslator;

@SuppressWarnings("serial")
public class LivejournalServlet extends BaseOutputServlet {

	@Override
	protected Result VerifyPassword(Json parameters) throws Exception {
		LJData data = parameters.FromJson(LJData.class);
		LJTranslator writer = new LJTranslator();
		return writer.Login(data.userName, data.password);
	}

	@Override
	protected Result SaveData(Json parameters) throws Exception {
		LJData ljData = parameters.FromJson(LJData.class);
		ljData.EncryptPassword();
		
		Key<LJData> result = DAO.SaveThing(ljData,GetUserID());
		return new Result("Saved!",result.getId());
	}

	@Override
	protected LJPost CreatePost(Json parameters, long userID) {
		LJPost post = parameters.FromJson(LJPost.class);
		post.setParent(userID);
		return post;
	}
	
	@Override
	protected Result SavePost(Json parameters) throws Exception {
		LJPost post = CreatePost(parameters,GetUserID());
	
		Key<LJPost> result = DAO.SaveThing(post, GetUserID());
		
		return new Result("Saved!",result.getId());
	}

	
	@Override
	protected Result UpdateData(Json parameters) throws Exception {
		LJData newData = parameters.FromJson(LJData.class);
		LJData existingLJData = DAO.LoadThing(LJData.class, newData.getId(), GetUserID());
		
		
		existingLJData.password = newData.password;
		existingLJData.EncryptPassword();
		existingLJData.timeZone = newData.timeZone;
		
		Key<LJData> result = DAO.SaveThing(existingLJData,GetUserID());
		return new Result("Saved!",result.getId());
	}

	@Override
	protected Result SaveTemplate(Json parameters) {
		LJTemplate template = parameters.FromJson(LJTemplate.class);
		Key<LJTemplate> result = DAO.SaveThing(template,GetUserID());
		return new Result("Saved!",result.getId());
	}
}