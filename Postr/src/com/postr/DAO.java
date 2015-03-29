package com.postr;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.Vector;

import org.joda.time.DateTime;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.QueryKeys;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;
import com.postr.DataTypes.BaseSaveable;
import com.postr.DataTypes.User;
import com.postr.DataTypes.UserEmail;
import com.postr.DataTypes.Inputs.DeliciousData;
import com.postr.DataTypes.Inputs.PinboardData;
import com.postr.DataTypes.Inputs.PinterestData;
import com.postr.DataTypes.Inputs.TestInputData;
import com.postr.DataTypes.Outputs.BaseOutput;
import com.postr.DataTypes.Outputs.BasePost;
import com.postr.DataTypes.Outputs.DWData;
import com.postr.DataTypes.Outputs.DWFeed;
import com.postr.DataTypes.Outputs.DWPost;
import com.postr.DataTypes.Outputs.LJData;
import com.postr.DataTypes.Outputs.LJFeed;
import com.postr.DataTypes.Outputs.LJPost;
import com.postr.DataTypes.Outputs.TestData;
import com.postr.DataTypes.Outputs.TestFeed;
import com.postr.DataTypes.Outputs.TestPost;
import com.postr.DataTypes.Outputs.WordPressData;
import com.postr.DataTypes.Outputs.WordPressFeed;
import com.postr.DataTypes.Outputs.WordPressPost;

public class DAO {
	static {
		JodaTimeTranslators.add(ObjectifyService.factory());
		ObjectifyService.register(BaseSaveable.class);
		ObjectifyService.register(BaseOutput.class);
		ObjectifyService.register(LJData.class);
		ObjectifyService.register(DWData.class);
		ObjectifyService.register(WordPressData.class);
		ObjectifyService.register(DeliciousData.class);
		ObjectifyService.register(PinboardData.class);
		ObjectifyService.register(PinterestData.class);
		ObjectifyService.register(TestInputData.class);
		ObjectifyService.register(UserEmail.class);
		ObjectifyService.register(User.class);
		ObjectifyService.register(DWPost.class);
		ObjectifyService.register(LJPost.class);
		ObjectifyService.register(TestPost.class);
		ObjectifyService.register(WordPressPost.class);
		ObjectifyService.register(TestData.class);
		ObjectifyService.register(LJFeed.class);
		ObjectifyService.register(DWFeed.class);
		ObjectifyService.register(TestFeed.class);
		ObjectifyService.register(WordPressFeed.class);
		ObjectifyService.register(SiteData.class);
	}

	public static <T extends BaseSaveable> Key<T> SaveThing(T thing, Long userID){
		thing.setParent(userID);
		return ofy().save().entity(thing).now();
	}

	public static <T extends BaseSaveable> Key<T> SaveThing(T thing) throws Exception{
		if(!thing.hasParent()){
			throw new Exception("Tried to save an object that doesn't have a parent.");
		}
		return ofy().save().entity(thing).now();
	}
	public static <T extends BaseSaveable> T LoadThing(Class<T> clazz, Long id, Long userID) throws Exception{
		T retrieved = ofy().load().type(clazz).id(id).now();
		if (retrieved instanceof User) {
			if(((User)retrieved).getId() != userID){
				throw new Exception("User ID did not match - possible security attack");
			}
		}else{
			if (retrieved.getParent() != userID) {
				throw new Exception("Parent ID did not match - possible security attack");
			}
		}
		return retrieved;
	}

	public static <T extends BaseSaveable> T LoadThing(Class<T> clazz, Long id) throws Exception{
		return ofy().load().type(clazz).id(id).now();
	}
	
	public static <T extends BaseSaveable> List<T> LoadThings(Class<T> clazz, Long userID){
		return ofy().load().type(clazz).filter("parent", userKey(userID)).list();
	}

	private static Key<User> userKey(long userID){
		return Key.create(User.class,userID);
	}

	static  void RemoveThing(Long id){
		ofy().delete().type(BaseSaveable.class).id(id);
	}

	static long GetUserID(String persona) throws Exception {
		LogHandler.info("Fetching data for "+persona);
		List<Key<UserEmail>> email = ofy().load().type(UserEmail.class).filter("email",persona).keys().list();
		LogHandler.info("Found "+email.size() + " entries");
		if (email.size() == 0) {
			LogHandler.info("Creating entry");
			User user = new User();
			long parent = ofy().save().entity(user).now().getId();
			UserEmail emailToSave = new UserEmail();
			emailToSave.email = persona;
			emailToSave.setParent(parent);
			ofy().save().entity(emailToSave).now();
			return parent;
		}
		if(email.size() == 1){
			LogHandler.info("Retrieving entry for "+email.get(0).getId());
			return ofy().load().key(email.get(0)).now().getParent();
		}
		throw new Exception("Multiple emails found with address: "+persona);
	}
	
	public static List<Long> LoadPostsInQueue(){
		QueryKeys<BasePost> query = ofy().load().type(BasePost.class)
				.filter("postingTime <=", DateTime.now())
				.filter("awaitingPostingTime",true).keys();
		Vector<Long> outputList = new Vector<Long>();
		for (Key<BasePost> feedKey : query) {
			outputList.add(feedKey.getId());
		}
		return outputList;
	}

	public static List<Long> LoadFeedsInQueue(){
		QueryKeys<BasePost> query = ofy().load().type(BasePost.class)
				.filter("postingTime <=", DateTime.now())
				.filter("active",true).keys();
		Vector<Long> outputList = new Vector<Long>();
		for (Key<BasePost> feedKey : query) {
			outputList.add(feedKey.getId());
		}
		return outputList;
	}
	
	static User GetUser(String persona) throws Exception{
		long userID = GetUserID(persona);
		LogHandler.info("User ID located - " + userID);
		User user = LoadThing(User.class, userID, userID);
		LogHandler.info("User loader - " + user.getId());
		return user;
	}
	
	private static SiteData siteData;
	
	public static SiteData GetSiteData() throws Exception{
		if (siteData == null) {
			List<SiteData> siteDataList = ofy().load().type(SiteData.class).list();
			if(siteDataList.size() == 1){
				siteData = siteDataList.get(0);
			}
			else if (siteDataList.isEmpty()) {
				siteData = SiteData.CreateDefault();
				ofy().save().entity(siteData).now();
			}
			else{
				throw new Exception("Multiple Site Data entries found.  WTF?");
			}
		}
		return siteData;
	}
	
	public static void SaveUser(User user) {
		ofy().save().entity(user).now();
	}
}
