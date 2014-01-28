package com.postr.DataTypes.Outputs;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.postr.DataTypes.PasswordEncryptor;

@EntitySubclass(index=true)
public class TestData extends BaseOutput{
	public void EncryptPassword(){
		password = PasswordEncryptor.MD5Hex(password);
	}
	
	@Override
	public String getSiteName() {
		return "Test";
	}
}
