package com.postr;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.postr.DataTypes.BaseSaveable;
import com.postr.DataTypes.DWData;
import com.postr.DataTypes.LJData;
import static com.googlecode.objectify.ObjectifyService.ofy;

class DAO {
static {
	ObjectifyService.register(BaseSaveable.class);
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

	
	
}
