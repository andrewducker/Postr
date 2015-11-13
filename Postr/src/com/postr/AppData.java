package com.postr;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

import org.apache.ws.commons.util.Base64;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.DataTypes.BaseSaveable;

@SuppressWarnings("serial")
@Subclass(index = true)
public class AppData extends BaseSaveable {
	public String HMACKey;
	public String Administrator;
	public int CookieTimeout;
	public String wordPressClientId;
	public String wordPressClientSecret;

	static AppData CreateDefault() throws NoSuchAlgorithmException {
		AppData siteData = new AppData();
		siteData.HMACKey = Base64.encode(KeyGenerator.getInstance("HmacSHA256")
				.generateKey().getEncoded());
		siteData.Administrator = "Andrew@Ducker.org.uk";
		siteData.CookieTimeout = 60 * 60 * 24 * 28; // 28 days
		siteData.wordPressClientId = "";
		siteData.wordPressClientSecret = "";
		return siteData;
	}

	private static AppData currentSiteData;

	public static AppData Current() throws Exception {
		if (currentSiteData == null) {
			currentSiteData = DAO.getAppData();
		}
		return currentSiteData;
	}
}
