package com.postr.DataTypes;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class BaseSaveable {
	//Must be Long, not long, otherwise not auto-generated.
	private @Id Long id;
	public long getId() {
		return id;
	}

	public void setParent(long parentID) {
		this.parent = Key.create(User.class, parentID);
	}
	
	public long getParent(){
		return parent.getId();
	}
	
	private @Index
	transient Key<User> parent;
	
	protected BaseSaveable(){}

	
}