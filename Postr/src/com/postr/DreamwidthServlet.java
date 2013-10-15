package com.postr;

import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.DWData;
import com.postr.DataTypes.Outputs.DWPost;
import com.postr.Translators.DWTranslator;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class DreamwidthServlet extends BaseOutputServlet {

	@Override
	protected Result VerifyPassword(String parameters) throws Exception {
		DWData data = Json.FromJson(parameters,DWData.class);
		DWTranslator writer = new DWTranslator();
		return writer.Login(data.userName, data.password);
	}	

	@Override
	protected Result SaveData(String parameters) throws Exception {
		DWData dwData = Json.FromJson(parameters,DWData.class);
		dwData.EncryptPassword();
		
		Key<DWData> result = DAO.SaveThing(dwData,GetUserID());
		return new Result("Saved!",result.getId());
	}

	@Override
	protected DWPost CreatePost(String parameters, long userID) {
		DWPost post = Json.FromJson(parameters,DWPost.class);
		post.setParent(userID);
		return post;
	}
	
	@Override
	protected Result SavePost(String parameters) throws Exception {
		DWPost post = CreatePost(parameters, GetUserID());
		
		Key<DWPost> result = DAO.SaveThing(post, GetUserID());
		
		return new Result("Saved!",result.getId());
	}
	
	@Override
	protected Result UpdateData(String parameters) throws Exception {
		DWData newData = Json.FromJson(parameters,DWData.class);
		DWData existingDWData = DAO.LoadThing(DWData.class, newData.getId(), GetUserID());
		existingDWData.password = newData.password;
		existingDWData.EncryptPassword();
		existingDWData.timeZone = newData.timeZone;
		
		DAO.SaveThing(existingDWData,GetUserID());
		return Result.Success("Saved"); 
	}
}
