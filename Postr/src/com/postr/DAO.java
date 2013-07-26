package com.postr;

import java.util.List;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.postr.DataTypes.*;

import static com.googlecode.objectify.ObjectifyService.ofy;

class DAO {
static {
	ObjectifyService.register(BaseSaveable.class);
	ObjectifyService.register(BaseOutput.class);
	ObjectifyService.register(LJData.class);
	ObjectifyService.register(DWData.class);
	ObjectifyService.register(DeliciousData.class);
	ObjectifyService.register(PinboardData.class);
	ObjectifyService.register(UserEmail.class);
	ObjectifyService.register(User.class);
}

public static <T extends BaseSaveable> Key<T> SaveThing(T thing, Long userID){
	thing.setParent(userID);
	return ofy().save().entity(thing).now();
}

public static Key<User> SaveUser(User user){
	return ofy().save().entity(user).now();
}
 

public static <T extends BaseSaveable> T LoadThing(Class<T> clazz, Long key, Long userID){
	return ofy().load().type(clazz).parent(userKey(userID)).id(key).get();
}

public static <T extends BaseSaveable> List<T> LoadThings(Class<T> clazz, Long userID){
	return ofy().load().type(clazz).ancestor(userKey(userID)).list();
}

public static <T extends BaseSaveable> void RemoveThing(Class<T> clazz, Long key, Long userID){
	ofy().delete().type(clazz).parent(userKey(userID)).id(key);
}

private static Key<User> userKey(long userID){
	return Key.create(User.class,userID);
}

public static  void RemoveThing(Long key, long userID){
	ofy().delete().type(BaseSaveable.class).parent(userKey(userID)).id(key);
}

public static long GetUserID(String persona) throws Exception {
	List<Key<UserEmail>> email = ofy().load().type(UserEmail.class).filter("email",persona).keys().list();
	if (email.size() == 0) {
		User user = new User();
		long parent = ofy().save().entity(user).now().getId();
		UserEmail emailToSave = new UserEmail();
		emailToSave.setEmail(persona);
		emailToSave.setParent(parent);
		ofy().save().entity(emailToSave).now();
		return parent;
	}
	if(email.size() == 1){
		return email.get(0).getParent().getId();
	}
	
	throw new Exception("Multiple emails found with address: "+persona);
}
}
