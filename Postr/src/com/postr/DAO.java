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
}

public static <T extends BaseSaveable> Key<T> SaveThing(T thing, String persona){
	thing.setPersona(persona);
	return ofy().save().entity(thing).now();
}

public static <T extends BaseSaveable> T LoadThing(Class<T> clazz, Long key){
	return ofy().load().type(clazz).id(key).get();
}

public static <T extends BaseSaveable> List<T> LoadThings(Class<T> clazz, String persona){
	return ofy().load().type(clazz).filter("persona", persona).list();
}

public static <T extends BaseSaveable> void RemoveThing(Class<T> clazz, Long key){
	ofy().delete().type(clazz).id(key);
}

public static  void RemoveThing(Long key){
	ofy().delete().type(BaseSaveable.class).id(key);
}
}
