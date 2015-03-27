package com.postr.Translators;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcHttpTransportException;

import com.postr.LogHandler;
import com.postr.MessageLogger;
import com.postr.Result;
import com.postr.DataTypes.Outputs.WordPressData;


public class MetaWebLogTranslator {
	
		protected String serverURL;
		
		public MetaWebLogTranslator(String url){
			serverURL = url;
		}
		
		public Result Login(String userName, String password) throws Exception
		{
		    XmlRpcClient client = getClient();
		    try{
			    Object[] params = new Object[]{"notUsedByWordPress",userName,password};
			    
				client.execute("metaWeblog.getCategories", params);
		    } 
		    catch (XmlRpcHttpTransportException e){
		        URL url = new URL("http://www.realip.info/api/p/realip.php");
		        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		        String line;
		        StringBuilder sb = new StringBuilder();
		        while ((line = reader.readLine()) != null) {
		        	sb.append(line);
		        }
		        reader.close();

    	        LogHandler.info("Current address is " + sb.toString());
		    	LogHandler.logException(this.getClass(), e, "Exception while gathering initialised call params - " + e.getStatusCode() + " - " + e.getStatusMessage());
		    	return Result.Failure(e.getMessage());
		    }
		    catch (XmlRpcException e){
		    	if(e.getMessage().equals("Incorrect username or password.")){
		    		return Result.Failure(e.getMessage());
		    	}else{
		    		throw e;
		    	}
		    }

		    return Result.Success("Logged in.");
		}
		
		public Result MakePost(WordPressData data, String contents, String header, String[] tags) throws Exception{
			try {
			    XmlRpcClient client = getClient();
				HashMap<String,Object> postParams = new HashMap<String,Object>();
				postParams.put("title", header);
				postParams.put("description",contents);
				postParams.put("mt_keywords",tags);
				Object[] params = new Object[]{"NotUsedByWordPress",data.userName,data.GetDecryptedPassword(),postParams,true};
				String ret = (String) client.execute("metaWeblog.newPost", params);

				URL resultUrl = new URL(new URL(serverURL),"?p="+ret); 
				
				return Result.Success(resultUrl.toString());
			} catch (MalformedURLException e1) {
				MessageLogger.Severe(this,e1.getMessage());
				return Result.Failure("Malformed URL");
			}
		}

		private XmlRpcClient getClient() throws MalformedURLException {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(serverURL));
			config.setUserAgent("Daily Link Poster; Hosted on Google App Engine; contact andrew@ducker.org.uk; v1");
		    XmlRpcClient client = new XmlRpcClient();
		    client.setConfig(config);
			return client;
		}
		
}
