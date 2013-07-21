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

@SuppressWarnings("serial")
public class PersonaVerificationServlet extends BasePersonaSessionServlet {

	@Override
	protected void handlePost(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		resp.setContentType("text/plain");

		String assertion = req.getParameter("assertion");
		
		URL url = new URL("https://verifier.login.persona.org/verify");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write("assertion="+assertion+"&audience=http://localhost:8888");
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
        		SetPersona((String) response.get("email"));
			}
        	else{
        		SetPersona(null);
        	}
        	result = "Success!";
        } else {
        	SetPersona(null);
        	result = "Fail";
        }

		resp.getWriter().print(result);	
	}

}


