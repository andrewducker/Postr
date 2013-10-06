package com.postr;

import org.joda.time.DateTimeZone;

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
				DateTimeZone.forID(timeZone));
		
		Key<DWData> result = DAO.SaveThing(dwData,GetUserID());
		Json toReturn =Json.SuccessResult("Saved!"); 
		toReturn.setData("id", result.getId());
		return toReturn;
	}

	@Override
	protected DWPost CreatePost(Json parameters, long userID) {
		String contents = parameters.getString("contents");
		String subject = parameters.getString("subject");
		String tags = parameters.getString("tags");
		long output = parameters.getLong("output");
		LivejournalVisibilityTypes visibility =  LivejournalVisibilityTypes.valueOf(parameters.getString("visibility"));
		DWPost post = new DWPost(subject, contents, tags,visibility, output);
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
		String password = parameters.getString("password");
		String timeZone = parameters.getString("timeZone");
		DWData existingDWData = DAO.LoadThing(DWData.class, key, GetUserID());
		
		DWData newDWData = new DWData(existingDWData,password,DateTimeZone.forID(timeZone));
		
		DAO.SaveThing(newDWData,GetUserID());
		return Json.SuccessResult("Saved!"); 
	}
}
