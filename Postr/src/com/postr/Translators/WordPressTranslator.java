package com.postr.Translators;

import com.postr.Result;
import com.postr.DataTypes.Outputs.WordPressData;


public class WordPressTranslator {
		public Result MakePost(WordPressData data, String contents, String header, String[] tags) throws Exception{
//			try {
//			    XmlRpcClient client = getClient();
//				HashMap<String,Object> postParams = new HashMap<String,Object>();
//				postParams.put("title", header);
//				postParams.put("description",contents);
//				postParams.put("mt_keywords",tags);
//				Object[] params = new Object[]{"NotUsedByWordPress",data.userName,data.GetDecryptedPassword(),postParams,true};
//				String ret = (String) client.execute("metaWeblog.newPost", params);
//
//				URL resultUrl = new URL(new URL(serverURL),"?p="+ret); 
				String resultUrl="Fake!";
				return Result.Success(resultUrl.toString());
//			}
			}

}
