package com.postr;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.UserData;

@SuppressWarnings("serial")
public class PersonaVerificationServlet extends BasePersonaSessionServlet {

	@Override
	protected void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		resp.setContentType("text/plain");

		String assertion = req.getParameter("assertion");
		
		URL url = new URL("https://verifier.login.persona.org/verify");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write("assertion="+assertion+"&audience="+req.getServerName());
        writer.close();

        String result;
        
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        	ByteArrayInputStream  json =  (ByteArrayInputStream)connection.getContent();
        	BufferedReader br
        	= new BufferedReader(
        		new InputStreamReader(json));
 
    	StringBuilder sb = new StringBuilder();
 
    	String line;
    	while ((line = br.readLine()) != null) {
    		sb.append(line);
    		}
    		
        	@SuppressWarnings("unchecked")
			StringMap<Object> response = (StringMap<Object>)new Gson().fromJson(sb.toString(), Object.class);
        	if (response.containsKey("status") && response.get("status").equals("okay")) {
        		String persona =(String) response.get("email"); 
        		SetPersona(persona);
        		UserData userData = new UserData(GetPersona(),GetUserID());
        		result = userData.AsJson();
			}
        	else{
        		SetPersona(null);
        		result = Json.ErrorResult("User not validated").toString();
        	}
        } else {
        	SetPersona(null);
        	result = Json.ErrorResult("Could not connect to validation server").toString();
        }

		resp.getWriter().print(result);	
	}

}


