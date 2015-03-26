package com.postr;

import com.googlecode.objectify.Key;
import com.postr.DataTypes.Json;
import com.postr.DataTypes.Outputs.WordPressData;
import com.postr.DataTypes.Outputs.WordPressFeed;
import com.postr.DataTypes.Outputs.WordPressPost;
import com.postr.Translators.MetaWebLogTranslator;

@SuppressWarnings("serial")
public class WordpressServlet extends BaseOutputServlet {

	Class<WordPressData> dataClass = WordPressData.class;
	Class<WordPressFeed> feedClass = WordPressFeed.class;
	Class<WordPressPost> postClass = WordPressPost.class;
	
	@Override
	protected Result VerifyPassword(Json parameters) throws Exception {
		WordPressData data = parameters.FromJson(dataClass);
		MetaWebLogTranslator writer = new MetaWebLogTranslator(data.url);
		return writer.Login(data.userName, data.password);
	}

	@Override
	protected Result SaveData(Json parameters) throws Exception {
		WordPressData data = parameters.FromJson(dataClass);
		data.EncryptPassword();
		
		Key<WordPressData> result = DAO.SaveThing(data,GetUserID());
		return new Result("Saved!",result.getId());
	}

	@Override
	protected Result SaveFeed(Json parameters) throws Exception {
		WordPressFeed feed = parameters.FromJson(feedClass);
		feed.awaitingPostingTime = feed.active;
		Key<WordPressFeed> result = DAO.SaveThing(feed,GetUserID());
		return new Result("Saved!",result.getId());
	}
	
	@Override
	protected WordPressPost CreatePost(Json parameters) {
		WordPressPost post = parameters.FromJson(postClass);
		return post;
	}
	
	@Override
	protected Result SavePost(Json parameters) throws Exception {
		WordPressPost post = CreatePost(parameters);
	
		Key<WordPressPost> result = DAO.SaveThing(post, GetUserID());
		
		return new Result("Saved!",result.getId());
	}

	@Override
	protected Result UpdateData(Json parameters) throws Exception {
		WordPressData newData = parameters.FromJson(dataClass);
		WordPressData existingData = DAO.LoadThing(dataClass, newData.getId(), GetUserID());
		
		existingData.password = newData.password;
		existingData.EncryptPassword();
		
		Key<WordPressData> result = DAO.SaveThing(existingData,GetUserID());
		return new Result("Saved!",result.getId());
	}
}