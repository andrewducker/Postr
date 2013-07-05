package com.postr;

import java.net.HttpURLConnection;
import java.net.URL;
import com.googlecode.objectify.Key;
import com.postr.DataTypes.DeliciousData;
import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
public class DeliciousServlet extends BaseInputServlet {

	@Override
	protected Json UpdateData(Json parameters) throws Exception {
		Json toReturn =Json.ErrorResult("There is nothing updateable about a Delicious Input"); 
		return toReturn;
	}

	@Override
	protected Json VerifyUserExists(Json parameters) throws Exception {
		URL url = new URL("http://feeds.delicious.com/v2/rss/"+parameters.getString("userName"));
		try{
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			int code = connection.getResponseCode();
			if (code == 200) {
				return Json.SuccessResult("User verified.");			
			}
		}
		catch(Exception e){
			return Json.ErrorResult("Failed to find user.");
		}
		return Json.ErrorResult("Failed to find user.");
	}

	@Override
	protected Json SaveData(Json parameters) throws Exception {
		String userName = parameters.getString("userName");
		DeliciousData deliciousData = new DeliciousData(userName);
		
		Key<DeliciousData> result = DAO.SaveThing(deliciousData,GetPersona());
		Json toReturn = Json.SuccessResult("Saved!");
		toReturn.setData("key", result.getId());
		return toReturn;
	}

}
