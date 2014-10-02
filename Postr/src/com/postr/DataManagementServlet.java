package com.postr;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.Feed;

@SuppressWarnings("serial")
public class DataManagementServlet extends BasePersonaSessionServlet {

	@Override
	protected Result ProcessRequest(Json parameters) throws Exception {
		
		Request request = parameters.FromJson(Request.class);

		MethodTypes methodType = MethodTypes.valueOf(request.method);
		switch (methodType) {
		case RemoveData:
			return RemoveData(request);
		case SaveFeed:
			return SaveFeed(parameters);
		default:
			throw new Exception("No such method found: "+request.method);
		}
	}
	
	private Result SaveFeed(Json parameters){
		Feed feed = parameters.FromJson(Feed.class);
		Key<Feed> result = DAO.SaveThing(feed,GetUserID());
		return new Result("Saved!",result.getId());

	}
	
	private Result RemoveData(Request parameters) {
		Long key = parameters.id;
		DAO.RemoveThing(key);
		return Result.Success("Removed!");
	}
}
