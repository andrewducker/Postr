package com.postr;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.DataTypes.BaseSaveable;

@SuppressWarnings("serial")
@Subclass(index=true)
public class SiteData extends BaseSaveable {
	public String HMACKey;
	public String Administrator;
	public int CookieTimeout;
}
