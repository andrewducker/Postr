package com.postr;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.PersonaAssertion;
import com.postr.DataTypes.UserData;

@SuppressWarnings("serial")
public class PersonaVerificationServlet extends BasePersonaSessionServlet {

	@Override
	protected Result ProcessRequest(Json parameters) throws Exception {
		
		PersonaAssertion personaAssertion = parameters.FromJson(PersonaAssertion.class);
		
		URL url = new URL("https://verifier.login.persona.org/verify");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");

		OutputStreamWriter writer = new OutputStreamWriter(
				connection.getOutputStream());
		writer.write("assertion=" + personaAssertion.assertion + "&audience="
				+ serverName);
		writer.close();

		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			ByteArrayInputStream json = (ByteArrayInputStream) connection
					.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(json));

			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			@SuppressWarnings("unchecked")
			StringMap<Object> response = (StringMap<Object>) new Gson()
					.fromJson(sb.toString(), Object.class);
			if (response.containsKey("status")
					&& response.get("status").equals("okay")) {
				String persona = (String) response.get("email");
				SetPersona(persona);
				UserData userData = new UserData(GetPersona(), GetUserID());
				return new Result("Here's your data",userData);
			} else {
				SetPersona(null);
				return Result.Failure("User not validated");
			}
		} else {
			SetPersona(null);
			return Result.Failure("Could not connect to validation server");
		}
	}

}
