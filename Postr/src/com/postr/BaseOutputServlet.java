
package com.postr;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.BasePost;

@SuppressWarnings("serial")
abstract class BaseOutputServlet extends BaseJSONServlet {


	@Override
	protected Result ProcessRequest(String parameters) throws Exception
	{
		String method = Json.FromJson(parameters, Request.class).method;

		MethodTypes methodType = MethodTypes.valueOf(method);
		switch (methodType) {
		case VerifyPassword:
			return VerifyPassword(parameters);
		case SaveData:
			return SaveData(parameters);
		case UpdateData:
			return UpdateData(parameters);
		case MakePost:
			return MakePost(parameters);
		case SavePost:
			return SavePost(parameters);
		default:
			throw new Exception("No such method found: "+method);
		}

	}
	
	private Result MakePost(String parameters){
		BasePost post = CreatePost(parameters, GetUserID());
		post.MakePost();
		post.postingTime = DateTime.now(DateTimeZone.UTC);
		DAO.SaveThing(post, GetUserID());
		post.result.postingTime = post.postingTime.getMillis();
		return post.result;
	}
	
	protected abstract Result UpdateData	(String parameters) throws Exception;

	protected abstract Result VerifyPassword(String parameters) throws Exception;
	
	protected abstract Result SaveData(String parameters) throws Exception;
	
	protected abstract BasePost CreatePost(String parameters, long userID);

	protected abstract Result SavePost(String parameters) throws Exception;
}