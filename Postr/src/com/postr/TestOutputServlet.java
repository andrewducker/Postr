package com.postr;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.TestData;
import com.postr.DataTypes.Outputs.TestFeed;
import com.postr.DataTypes.Outputs.TestPost;

@SuppressWarnings("serial")
public class TestOutputServlet extends BaseOutputServlet {

	@Override
	protected Result VerifyPassword(Json parameters) throws Exception {
		return  Result.Success("Logged in as Test User");
		}

	@Override
	protected Result SaveData(Json parameters) throws Exception {
		TestData ljData = parameters.FromJson(TestData.class);
		ljData.EncryptPassword();
		
		Key<TestData> result = DAO.SaveThing(ljData,GetUserID());
		return new Result("Saved!",result.getId());
	}
	
	@Override
	protected Result SaveFeed(Json parameters) throws Exception {
		TestFeed testFeed = parameters.FromJson(TestFeed.class);
		
		Key<TestFeed> result = DAO.SaveThing(testFeed,GetUserID());
		return new Result("Saved!",result.getId());
	}

	@Override
	protected TestPost CreatePost(Json parameters) {
		TestPost post = parameters.FromJson(TestPost.class);
		post.timeZone = GetTimeZone();
		return post;
	}
	
	@Override
	protected Result SavePost(Json parameters) throws Exception {
		TestPost post = CreatePost(parameters);
	
		Key<TestPost> result = DAO.SaveThing(post, GetUserID());
		
		return new Result("Saved!",result.getId());
	}

	
	@Override
	protected Result UpdateData(Json parameters) throws Exception {
		TestData newData = parameters.FromJson(TestData.class);
		TestData existingLJData = DAO.LoadThing(TestData.class, newData.getId(), GetUserID());
		
		
		existingLJData.password = newData.password;
		existingLJData.EncryptPassword();
		
		Key<TestData> result = DAO.SaveThing(existingLJData,GetUserID());
		return new Result("Saved!",result.getId());
	}

}