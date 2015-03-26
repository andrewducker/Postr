package com.postr;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

import org.apache.ws.commons.util.Base64;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.DataTypes.BaseSaveable;

@SuppressWarnings("serial")
@Subclass(index = true)
public class SiteData extends BaseSaveable {
	public String HMACKey;
	public String Administrator;
	public int CookieTimeout;

	static SiteData CreateDefault() throws NoSuchAlgorithmException {
		SiteData siteData = new SiteData();
		siteData.HMACKey = Base64.encode(KeyGenerator.getInstance("HmacSHA256")
				.generateKey().getEncoded());
		siteData.Administrator = "Andrew@Ducker.org.uk";
		siteData.CookieTimeout = 60 * 60 * 24 * 28; // 28 days
		return siteData;
	}

	private static SiteData currentSiteData;

	public static SiteData Current() throws Exception {
		if (currentSiteData == null) {
			currentSiteData = DAO.GetSiteData();
		}
		return currentSiteData;
	}
}
