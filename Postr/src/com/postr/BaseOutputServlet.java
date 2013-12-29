
package com.postr;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.BasePost;

@SuppressWarnings("serial")
abstract class BaseOutputServlet extends BasePersonaSessionServlet {


	@Override
	protected Result ProcessRequest(Json parameters) throws Exception
	{
		String method = parameters.FromJson(Request.class).method;

		MethodTypes methodType = MethodTypes.valueOf(method);
		switch (methodType) {
		case Verify:
			return VerifyPassword(parameters);
		case SaveData:
			return SaveData(parameters);
		case UpdateData:
			return UpdateData(parameters);
		case MakePost:
			return MakePost(parameters);
		case SavePost:
			return SavePost(parameters);
		case SaveTemplate:
			return SaveTemplate(parameters);
		default:
			throw new Exception("No such method found: "+method);
		}
	}
	
	private Result MakePost(Json parameters){
		BasePost post = CreatePost(parameters, GetUserID());
		post.MakePost();
		post.postingTime = DateTime.now(DateTimeZone.UTC);
		DAO.SaveThing(post, GetUserID());
		post.result.postingTime = post.postingTime.getMillis();
		return post.result;
	}
	

	protected abstract Result SaveTemplate(Json parameters);

	protected abstract Result UpdateData	(Json parameters) throws Exception;

	protected abstract Result VerifyPassword(Json parameters) throws Exception;
	
	protected abstract Result SaveData(Json parameters) throws Exception;
	
	protected abstract BasePost CreatePost(Json parameters, long userID);

	protected abstract Result SavePost(Json parameters) throws Exception;
}