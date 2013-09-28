
package com.postr;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.BasePost;

@SuppressWarnings("serial")
public abstract class BaseOutputServlet extends BaseJSONServlet {


	@Override
	protected Json ProcessRequest(Json parameters) throws Exception
	{
		String method = parameters.getString("method");

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
		case UpdatePost:
			return UpdatePost(parameters);
		default:
			throw new Exception("No such method found: "+method);
		}

	}
	
	private Json MakePost(Json parameters){
		BasePost post = CreatePost(parameters, GetUserID());
		post.MakePost();
		DAO.SaveThing(post, GetUserID());
		return Json.Result(post.getResult());
	}
	
	protected abstract Json UpdateData	(Json parameters) throws Exception;

	protected abstract Json VerifyPassword(Json parameters) throws Exception;
	
	protected abstract Json SaveData(Json parameters) throws Exception;
	
	protected abstract BasePost CreatePost(Json parameters, long userID);

	protected abstract Json SavePost(Json parameters) throws Exception;

	protected abstract Json UpdatePost(Json parameters) throws Exception;
}