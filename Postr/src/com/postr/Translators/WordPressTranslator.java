package com.postr.Translators;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.goebl.david.Webb;
import com.postr.Result;
import com.postr.DataTypes.Outputs.WordPressData;


public class WordPressTranslator {
	public Result MakePost(WordPressData data, String contents, String header, String[] tags) throws Exception{
		Webb webb = Webb.create();
		JSONObject result = webb.post("https://public-api.wordpress.com/rest/v1.1/sites/" + data.blogId + "/posts/new")
				.param("title", header)
				.param("content", contents)
				.param("tags", StringUtils.join(tags,","))
				.header("Authorization", "Bearer "+data.password)
				.asJsonObject().getBody();

		String resultUrl=result.getString("URL");
		return Result.Success(resultUrl.toString());
	}
}