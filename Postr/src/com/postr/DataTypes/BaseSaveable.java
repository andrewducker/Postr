package com.postr.DataTypes;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class BaseSaveable {
	//Must be Long, not long, otherwise not auto-generated.
	@Id Long id;
	public long getId() {
		return id;
	}

	public void setParent(long userID) {
		this.parent = Key.create(User.class, userID);
	}

	
	@Parent
	Key<User> parent;
	
	protected BaseSaveable(){}

	public BaseSaveable(BaseSaveable existingData) {
		this.id = existingData.id;
	}
}
