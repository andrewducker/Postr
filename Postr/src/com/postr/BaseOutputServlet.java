package com.postr;

import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.BasePost;
import com.postr.DataTypes.Outputs.ResultData;
import com.postr.DataTypes.Outputs.ResultStateEnum;

@SuppressWarnings("serial")
abstract class BaseOutputServlet extends BasePersonaSessionServlet {

	@Override
	protected Result ProcessRequest(Json parameters) throws Exception {
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
			throw new Exception("No such method found: " + method);
		}
	}

	private Result MakePost(Json parameters) throws Exception {
		BasePost post = CreatePost(parameters, GetUserID());
		
		if (post.hasbeenSaved()) {
			BasePost savedPost = DAO.LoadThing(BasePost.class, post.getId(),
					GetUserID());
			if (!savedPost.awaitingPostingTime) {
				return Result
						.Failure("This post has already been posted, and can no longer be updated.");
			}
		}
		ResultStateEnum state = post.PostOrSave();

		post.result.data = new ResultData(post.getId(), state);
		return post.result;
	}

	protected abstract Result SaveTemplate(Json parameters);

	protected abstract Result UpdateData(Json parameters) throws Exception;

	protected abstract Result VerifyPassword(Json parameters) throws Exception;

	protected abstract Result SaveData(Json parameters) throws Exception;

	protected abstract BasePost CreatePost(Json parameters, long userID);

	protected abstract Result SavePost(Json parameters) throws Exception;
}