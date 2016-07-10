package com.postr;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.goebl.david.Webb;
import com.postr.DataTypes.Outputs.WordPressData;


@SuppressWarnings("serial")
public class WordPressAuthorisationComplete extends BaseUserSessionServlet {

@Override
void handleRequest() throws Exception {
	AppData appData = DAO.getAppData();
	
	HttpServletRequest req = getRequest();
	String code = req.getParameter("code");
	String error = req.getParameter("error");
	String serverUrl = req.getScheme()+"://"+req.getServerName();
	if(req.getServerPort() != 80 && req.getServerPort() != 443){
		serverUrl += ":" +req.getServerPort();
	}
	if(error != null && !error.isEmpty()){
		LogHandler.logWarning(this, "Failed to authorise to wordpress: " + error);
	}
	else if(code != null && !code.isEmpty())
	{
		Webb webb = Webb.create();
		JSONObject result = webb.post("https://public-api.wordpress.com/oauth2/token")
				.param("client_id", appData.wordPressClientId)
				.param("redirect_uri", serverUrl+"/WordPressAuthorisationComplete")
				.param("client_secret", appData.wordPressClientSecret)
				.param("code", code)
				.param("grant_type", "authorization_code").asJsonObject().getBody();
		String token = result.getString("access_token");
		String blogId = result.getString("blog_id");
		String blogUrl = result.getString("blog_url");
		
		WordPressData wordPressData = new WordPressData();
		wordPressData.userName = blogUrl;
		wordPressData.password = token;
		wordPressData.blogId = blogId;
		DAO.SaveThing(wordPressData, GetUserID());
	}else{
		LogHandler.logWarning(getClass(), "Something went wrong retrieving the authorisation information");
	}
	HttpServletResponse response = getResponse();
	response.sendRedirect(serverUrl);
}
	
	
}
