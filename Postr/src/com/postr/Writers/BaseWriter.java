package com.postr.Writers;

import java.util.List;

import com.postr.DataTypes.BaseSaveable;

	public abstract class BaseWriter extends BaseSaveable {
		public abstract String Write(String contents, String header,List<String> tags) throws Exception;
		public abstract String EncryptPassword(String password) throws Exception;
	}
