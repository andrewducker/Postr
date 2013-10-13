package com.postr.DataTypes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptor {

	static String Encrypt(String password) throws Exception{
		return MD5Hex(password);
	}
	public static String MD5Hex(String s){
        MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// will absolutely never happen.
		}
        byte[] digest = md5.digest(s.getBytes());
        return toHex(digest);
	}
    public static String toHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (int i = 0; i < a.length; i++) {
            sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
            sb.append(Character.forDigit(a[i] & 0x0f, 16));
        }
        return sb.toString();
    }

}
