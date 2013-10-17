package com.postr.Translators;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.GregorianChronology;

import com.postr.LivejournalVisibilityTypes;
import com.postr.MessageLogger;
import com.postr.Result;
import com.postr.DataTypes.PasswordEncryptor;
import com.postr.DataTypes.Outputs.LJData;


public class LJTranslator {

		protected String serverURL = "http://www.livejournal.com/interface/xmlrpc";
		
		@SuppressWarnings("unchecked")
		public Result Login(String userName, String password) throws Exception
		{
			if (userName.equals("test") && password.equals("test")) {
				return  Result.Success("Logged in as Test User");	
			}
		    
		    XmlRpcClient client = getClient();
		    password = EncryptPassword(password);
		    HashMap<String, Object> loginParams = getInitialisedCallParams(client, userName, password);
		    Object[] params = new Object[]{loginParams};
		    Map<String, String> postResult;
		    try{
		    	postResult =  (Map<String, String>) client.execute("LJ.XMLRPC.login", params);
		    } catch (XmlRpcException e){
		    	return Result.Failure(e.getMessage());
		    }
		    
		    if (postResult.get("success")=="FAIL"){
		    	return Result.Failure(postResult.get("errmsg"));
		    }
		    return Result.Success("Logged in as " + postResult.get("fullname"));
		}

		
		
		@SuppressWarnings("unchecked")
		public Result MakePost(LJData ljData, String contents, String header, String[] tags, LivejournalVisibilityTypes visibility, Boolean autoFormat){
		    XmlRpcClient client;
			try {
				client = getClient();
			} catch (MalformedURLException e1) {
				MessageLogger.Severe(this,e1.getMessage());
				return Result.Failure("Malformed URL");
			}
		    
		    if (ljData.userName.equalsIgnoreCase("test")) {
				return Result.Success("Test data, successfully not saved");
			}
		    
		    HashMap<String, Object> postParams;
			try {
				postParams = getInitialisedCallParams(client,ljData.userName,ljData.password);
			} catch (Exception e1) {
				MessageLogger.Severe(this,e1.getMessage());
				return Result.Failure("Error connecting to server.");
			}
		    
		    postParams.put("event", contents);
		    postParams.put("subject", header);
		    
		    switch (visibility) {
			case FriendsOnly:
				postParams.put("security","usemask");
				postParams.put("allowmask", 1);
				break;
			case Private:
			    postParams.put("security","private");
			    break;
			case Public:
			default:
				break;
			}
		    
		    DateTime calendar = new DateTime(GregorianChronology.getInstance(DateTimeZone.forID(ljData.timeZone)));
		    postParams.put("year",calendar.getYear());
		    postParams.put("mon",calendar.getMonthOfYear());
		    postParams.put("day",calendar.getDayOfMonth());
		    postParams.put("hour",calendar.getHourOfDay());
		    postParams.put("min",calendar.getMinuteOfHour());
		    
		    HashMap<String,Object> options = new HashMap<String,Object>();
		    String tagsToUse = "";
		    for (String tag : tags) {
		    	if(tagsToUse.length()>0)
		    	{
		    		tagsToUse+=",";
		    	}
				tagsToUse +=tag; 
			}
		    options.put("taglist", tagsToUse);
		    options.put("opt_preformatted", !autoFormat);
		    postParams.put("props",options);
		    
		    Object[] params = new Object[]{postParams};
		    Map<String, String> postResult;
		    try{
		    	postResult =  (Map<String, String>) client.execute("LJ.XMLRPC.postevent", params);
		    } catch (XmlRpcException e){
		    	if (e.getMessage().equals("Invalid password")){
		    		return Result.Failure("Invalid Password");
		    	} else{
					MessageLogger.Severe(this,e.getMessage());
		    		return Result.Failure("Error communicating with server: " + e.getMessage());
		    	}
		    }
		    
		    if (postResult.get("success")=="FAIL"){
		    	return Result.Failure(postResult.get("errmsg"));
		    }
		    return Result.Success("<A href=" + postResult.get("url")+ ">Link posted</A>");
		}

		@SuppressWarnings("unchecked")
		private HashMap<String, Object> getInitialisedCallParams(
				XmlRpcClient client, String userName, String password) throws XmlRpcException,
				NoSuchAlgorithmException {
			Map<String, String> challengeResult =  (Map<String, String>) client.execute("LJ.XMLRPC.getchallenge", new Object[0]);
		    
		    String challenge = (String) challengeResult.get("challenge");
		    
		    String response = PasswordEncryptor.MD5Hex(challenge+password);
	    
		    HashMap<String,Object> postParams = new HashMap<String,Object>();
		    postParams.put("username", userName);
		    postParams.put("auth_method", "challenge");
		    postParams.put("auth_challenge", challenge);
		    postParams.put("auth_response", response);
			return postParams;
		}



		private XmlRpcClient getClient() throws MalformedURLException {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(serverURL));
		    XmlRpcClient client = new XmlRpcClient();
		    client.setConfig(config);
			return client;
		}
		
		private String EncryptPassword(String password) throws Exception{
			return PasswordEncryptor.MD5Hex(password);
		}
}
