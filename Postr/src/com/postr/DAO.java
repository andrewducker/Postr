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
}

public static <T extends BaseSaveable> Key<T> SaveThing(T thing, Long userID){
	thing.setUserID(userID);
	return ofy().save().entity(thing).now();
}

public static <T extends BaseSaveable> T LoadThing(Class<T> clazz, Long key){
	return ofy().load().type(clazz).id(key).get();
}

public static <T extends BaseSaveable> List<T> LoadThings(Class<T> clazz, Long userID){
	return ofy().load().type(clazz).filter("userID", userID).list();
}

public static <T extends BaseSaveable> void RemoveThing(Class<T> clazz, Long key){
	ofy().delete().type(clazz).id(key);
}

public static  void RemoveThing(Long key){
	ofy().delete().type(BaseSaveable.class).id(key);
}

public static long GetUserID(String persona) throws Exception {
	List<Key<UserEmail>> email = ofy().load().type(UserEmail.class).filter("email",persona).keys().list();
	if (email.size() == 0) {
		UserEmail emailToSave = new UserEmail();
		emailToSave.setEmail(persona);
		ofy().save().entity(emailToSave).now();
		return emailToSave.getId();
	}
	if(email.size() == 1){
		return email.get(0).getId();
	}
	
	throw new Exception("Multiple emails found with address: "+persona);
}
}
