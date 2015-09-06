package com.postr;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;

import org.apache.ws.commons.util.Base64;
import org.joda.time.DateTime;

public class CookieHandler {

	public static Cookie GenerateCookie(String email) throws Exception {
		long now = DateTime.now().getMillis();
		long cookieTimeOut = AppData.Current().CookieTimeout;
		Long timeOutMillis = (long) (cookieTimeOut * 1000);
		Long expires = now + (timeOutMillis);
		String concatenated = email + "|" + expires + "|";
		String withHmac = concatenated + computeHmac(concatenated);
		Cookie cookie = new Cookie("PostrID", withHmac);
		cookie.setMaxAge((int) cookieTimeOut);
		return cookie;
	}

	public static String ParseCookie(String contents) throws Exception {
		// First we break down the cookie
		String[] parts = contents.split("\\|");
		String email = parts[0];
		long expires = Long.parseLong(parts[1]);
		String hmac = contents.substring(parts[0].length() + parts[1].length()
				+ 2);

		// Then we compare the HMAC, to make sure it's valid.
		String testedHmac = computeHmac(email + "|" + expires + "|");
		if (!hmac.equals(testedHmac)) {
			LogHandler.logWarning(CookieHandler.class,
					"Failed to parse HMAC for " + contents);
			return null;
		}
		DateTime cookieExpires = new DateTime(expires);
		if (!cookieExpires.isAfterNow()) {
			LogHandler.logWarning(CookieHandler.class, "Cookie expired at "
					+ cookieExpires.toString());
			return null;
		}
		LogHandler.info("Retrieved email from cookie - " + email);
		return email;

	}

	public static String computeHmac(String baseString) throws Exception {
		Mac mac = Mac.getInstance("HmacSHA256");
		String key = AppData.Current().HMACKey;
		SecretKeySpec secret = new SecretKeySpec(key.getBytes(),
				mac.getAlgorithm());
		mac.init(secret);
		byte[] digest = mac.doFinal(baseString.getBytes());
		return Base64.encode(digest);
	}
}