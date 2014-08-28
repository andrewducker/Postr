package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.Subclass;
import com.postr.DataTypes.PasswordEncryptor;

@Subclass(index=true)
public class LJData extends BaseOutput{
	public void EncryptPassword(){
		password = PasswordEncryptor.MD5Hex(password);
	}
	
	@Override
	public String getSiteName() {
		return "Livejournal";
	}
}
