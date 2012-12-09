package com.postr.Writers;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.postr.DataTypes.PasswordEncryptor;


public class LJWriter extends BaseWriter {

		protected LJWriter(){}
		
		private String userName;
		private String password;
		private TimeZone timeZone;
		private Boolean postPrivately;
		public LJWriter(String userName, String password, TimeZone timeZone, Boolean postPrivately) throws Exception{
			this.userName = userName;
			this.password = EncryptPassword(password);
			this.timeZone = timeZone;
			this.postPrivately = postPrivately;
		}
		
		protected String serverURL = "http://www.livejournal.com/interface/xmlrpc";
		
		
		@SuppressWarnings("unchecked")
		public String Login() throws Exception
		{
		    XmlRpcClient client = getClient();
		    
		    HashMap<String, Object> loginParams = getInitialisedCallParams(client);
		    Object[] params = new Object[]{loginParams};
		    Map<String, String> postResult;
		    try{
		    	postResult =  (Map<String, String>) client.execute("LJ.XMLRPC.login", params);
		    } catch (XmlRpcException e){
		    	return e.getMessage();
		    }
		    
		    if (postResult.get("success")=="FAIL"){
		    	return postResult.get("errmsg");
		    }
		    return "Logged in as " + postResult.get("fullname");
		}

		
		
		@SuppressWarnings("unchecked")
		public String Write(String contents, String header, List<String> tags)  throws Exception{
		    XmlRpcClient client = getClient();
		    
		    HashMap<String, Object> postParams = getInitialisedCallParams(client);
		    
		    postParams.put("event", contents);
		    postParams.put("subject", header) ;
		    if (postPrivately) {
			    postParams.put("security","private");
			}

		    Calendar calendar = Calendar.getInstance(timeZone);
		    postParams.put("year",calendar.get(Calendar.YEAR));
		    postParams.put("mon",calendar.get(Calendar.MONTH)+1);
		    postParams.put("day",calendar.get(Calendar.DAY_OF_MONTH));
		    postParams.put("hour",calendar.get(Calendar.HOUR_OF_DAY));
		    postParams.put("min",calendar.get(Calendar.MINUTE));
		    
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
		    options.put("opt_preformatted", true);
		    postParams.put("props",options);
		    
		    Object[] params = new Object[]{postParams};
		    Map<String, String> postResult;
		    try{
		    	postResult =  (Map<String, String>) client.execute("LJ.XMLRPC.postevent", params);
		    } catch (XmlRpcException e){
		    	if (e.getMessage().equals("Invalid password")){
		    		return "Invalid Password";
		    	} else{
		    		throw e;
		    	}
		    }
		    
		    if (postResult.get("success")=="FAIL"){
		    	return postResult.get("errmsg");
		    }
		    return "<A href=" + postResult.get("url")+ ">Link posted</A>";
		}



		@SuppressWarnings("unchecked")
		private HashMap<String, Object> getInitialisedCallParams(
				XmlRpcClient client) throws XmlRpcException,
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
		
		public String EncryptPassword(String password) throws Exception{
			return PasswordEncryptor.MD5Hex(password);
		}
}
