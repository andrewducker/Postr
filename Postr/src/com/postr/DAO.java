package com.postr;

import java.util.List;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;
import com.postr.DataTypes.*;
import com.postr.DataTypes.Inputs.*;
import com.postr.DataTypes.Outputs.*;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class DAO {
static {
	JodaTimeTranslators.add(ObjectifyService.factory());
	ObjectifyService.register(BaseSaveable.class);
	ObjectifyService.register(BaseOutput.class);
	ObjectifyService.register(LJData.class);
	ObjectifyService.register(DWData.class);
	ObjectifyService.register(DeliciousData.class);
	ObjectifyService.register(PinboardData.class);
	ObjectifyService.register(UserEmail.class);
	ObjectifyService.register(User.class);
	ObjectifyService.register(DWPost.class);
	ObjectifyService.register(LJPost.class);
}

public static <T extends BaseSaveable> Key<T> SaveThing(T thing, Long userID){
	thing.setParent(userID);
	return ofy().save().entity(thing).now();
}

public static Key<User> SaveUser(User user){
	return ofy().save().entity(user).now();
}
 

public static <T extends BaseSaveable> T LoadThing(Class<T> clazz, Long id, Long userID) throws Exception{
	T retrieved = ofy().load().type(clazz).id(id).get();
	if (retrieved.getParent() != userID) {
		throw new Exception("Parent ID did not match - possible security attack");
	}
	return retrieved;
}

public static <T extends BaseSaveable> List<T> LoadThings(Class<T> clazz, Long userID){
	return ofy().load().type(clazz).filter("parent", userKey(userID)).list();
}

public static <T extends BaseSaveable> void RemoveThing(Class<T> clazz, Long id){
	ofy().delete().type(clazz).id(id);
}

private static Key<User> userKey(long userID){
	return Key.create(User.class,userID);
}

public static  void RemoveThing(Long id){
	ofy().delete().type(BaseSaveable.class).id(id);
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
		return ofy().load().key(email.get(0)).get().getParent();
	}
	
	throw new Exception("Multiple emails found with address: "+persona);
}
}
