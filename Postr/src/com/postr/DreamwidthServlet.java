package com.postr;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.DWData;
import com.postr.DataTypes.Outputs.DWPost;
import com.postr.DataTypes.Outputs.DWTemplate;
import com.postr.Translators.DWTranslator;

@SuppressWarnings("serial")
public class DreamwidthServlet extends BaseOutputServlet {

	@Override
	protected Result VerifyPassword(Json parameters) throws Exception {
		DWData data = parameters.FromJson(DWData.class);
		DWTranslator writer = new DWTranslator();
		return writer.Login(data.userName, data.password);
	}	

	@Override
	protected Result SaveData(Json parameters) throws Exception {
		DWData dwData = parameters.FromJson(DWData.class);
		dwData.EncryptPassword();
		
		Key<DWData> result = DAO.SaveThing(dwData,GetUserID());
		return new Result("Saved!",result.getId());
	}
	
	@Override
	protected Result SaveTemplate(Json parameters) {
		DWTemplate template = parameters.FromJson(DWTemplate.class);
		Key<DWTemplate> result = DAO.SaveThing(template,GetUserID());
		return new Result("Saved!",result.getId());
	}

	@Override
	protected DWPost CreatePost(Json parameters, long userID) {
		DWPost post = parameters.FromJson(DWPost.class);
		post.setParent(userID);
		return post;
	}
	
	@Override
	protected Result SavePost(Json parameters) throws Exception {
		DWPost post = CreatePost(parameters, GetUserID());
		
		Key<DWPost> result = DAO.SaveThing(post, GetUserID());
		
		return new Result("Saved!",result.getId());
	}
	
	@Override
	protected Result UpdateData(Json parameters) throws Exception {
		DWData newData = parameters.FromJson(DWData.class);
		DWData existingDWData = DAO.LoadThing(DWData.class, newData.getId(), GetUserID());
		existingDWData.password = newData.password;
		existingDWData.EncryptPassword();
		existingDWData.timeZone = newData.timeZone;
		
		DAO.SaveThing(existingDWData,GetUserID());
		return Result.Success("Saved"); 
	}
}
