package com.postr;

import java.util.TimeZone;

import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.DWData;
import com.postr.DataTypes.Outputs.DWPost;
import com.postr.Translators.DWTranslator;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class DreamwidthServlet extends BaseOutputServlet {

	@Override
	protected Json VerifyPassword(Json parameters) throws Exception {
		String userName = parameters.getString("userName");
		String password = parameters.getString("password");
		DWTranslator writer = new DWTranslator();
		return writer.Login(userName, password);
	}	

	@Override
	protected Json SaveData(Json parameters) throws Exception {
		String userName = parameters.getString("userName");
		String password = parameters.getString("password");
		String timeZone = parameters.getString("timeZone");
		DWData dwData = new DWData(
				userName, password,
				TimeZone.getTimeZone(timeZone));
		
		Key<DWData> result = DAO.SaveThing(dwData,GetUserID());
		Json toReturn =Json.SuccessResult("Saved!"); 
		toReturn.setData("key", result.getId());
		return toReturn;
	}

	@Override
	protected Json MakePost(Json parameters) throws Exception {
		String contents = parameters.getString("contents");
		String subject = parameters.getString("subject");
		String tags = parameters.getString("tags");
		long output = parameters.getLong("output");
		
		DWPost post = new DWPost(subject, contents, tags, output);
		
		return post.MakePost();
	}
	
	@Override
	protected Json SavePost(Json parameters) throws Exception {
		String contents = parameters.getString("contents");
		String subject = parameters.getString("subject");
		String tags = parameters.getString("tags");
		long output = parameters.getLong("output");
		
		DWPost post = new DWPost(subject, contents, tags, output);
		
		Key<DWPost> result = DAO.SaveThing(post, GetUserID());
		
		Json toReturn = Json.SuccessResult("Saved!");
		toReturn.setData("key", result.getId());
		return toReturn;
	}

	@Override
	protected Json UpdateData(Json parameters) throws Exception {
		Long key = parameters.getLong("key");
		String password = parameters.getString("password");
		String timeZone = parameters.getString("timeZone");
		DWData existingDWData = DAO.LoadThing(DWData.class, key, GetUserID());
		
		DWData newDWData = new DWData(existingDWData,password,TimeZone.getTimeZone(timeZone));
		
		Key<DWData> result = DAO.SaveThing(newDWData,GetUserID());
		Json toReturn =Json.SuccessResult("Saved!"); 
		toReturn.setData("key", result.getId());
		return toReturn;
	}
}
