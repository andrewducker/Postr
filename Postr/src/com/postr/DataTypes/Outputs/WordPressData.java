package com.postr.DataTypes.Outputs;

import org.jasypt.util.text.BasicTextEncryptor;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.SiteData;

@SuppressWarnings("serial")
@Subclass(index = true)
public class WordPressData extends BaseOutput {
	@Override
	public String getSiteName() {
		return "WordPress";
	}

	public String url;
	
	public void EncryptPassword() throws Exception {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(SiteData.Current().HMACKey.replaceAll("\n",""));
		password = textEncryptor.encrypt(password);
	}

	public String GetDecryptedPassword()
			throws Exception {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(SiteData.Current().HMACKey.replaceAll("\n",""));
		return textEncryptor.decrypt(password);
	}
}
