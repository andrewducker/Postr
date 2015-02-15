package com.postr.DataTypes;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

@SuppressWarnings("serial")
@Entity
public class BaseSaveable implements java.io.Serializable {
	//Must be Long, not long, otherwise not auto-generated.
	private @Id Long id;
	public long getId() {
		return id;
	}
	
	public boolean hasbeenSaved(){
		return id != null;
	}

	public void setParent(long parentID) {
		this.parent = Ref.create(Key.create(User.class,parentID));
	}
	
	public long getParent(){
		return parent.get().getId();
	}
	
	public boolean hasParent(){
		return parent != null;
	}
	
	private @Index @Load transient Ref<User> parent;
	
	protected BaseSaveable(){}

	
}