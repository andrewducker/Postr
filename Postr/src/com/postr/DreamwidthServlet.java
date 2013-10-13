package com.postr;

import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.DWData;
import com.postr.DataTypes.Outputs.DWPost;
import com.postr.Translators.DWTranslator;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class DreamwidthServlet extends BaseOutputServlet {

	@Override
	protected Json VerifyPassword(Json parameters) throws Exception {
		DWData data = parameters.FromJson(DWData.class);
		DWTranslator writer = new DWTranslator();
		return writer.Login(data.userName, data.password);
	}	

	@Override
	protected Json SaveData(Json parameters) throws Exception {
		DWData dwData = parameters.FromJson(DWData.class);
		dwData.EncryptPassword();
		
		Key<DWData> result = DAO.SaveThing(dwData,GetUserID());
		Json toReturn =Json.SuccessResult("Saved!"); 
		toReturn.setData("id", result.getId());
		return toReturn;
	}

	@Override
	protected DWPost CreatePost(Json parameters, long userID) {
		DWPost post = parameters.FromJson(DWPost.class);
		post.setParent(userID);
		return post;
	}
	
	@Override
	protected Json SavePost(Json parameters) throws Exception {
		DWPost post = CreatePost(parameters, GetUserID());
		
		Key<DWPost> result = DAO.SaveThing(post, GetUserID());
		
		Json toReturn = Json.SuccessResult("Saved!");
		toReturn.setData("id", result.getId());
		return toReturn;
	}
	
	@Override
	protected Json UpdateData(Json parameters) throws Exception {
		Long key = parameters.getLong("id");
		DWData existingDWData = DAO.LoadThing(DWData.class, key, GetUserID());
		DWData newData = parameters.FromJson(DWData.class);
		existingDWData.password = newData.password;
		existingDWData.EncryptPassword();
		existingDWData.timeZone = newData.timeZone;
		
		DAO.SaveThing(existingDWData,GetUserID());
		return Json.SuccessResult("Saved!"); 
	}
}
