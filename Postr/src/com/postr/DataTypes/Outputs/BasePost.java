package com.postr.DataTypes.Outputs;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;
import com.postr.DAO;
import com.postr.Result;
import com.postr.DataTypes.BaseSaveable;

@Subclass(index = true)
public abstract class BasePost extends BaseSaveable {
	abstract void MakePost();
	public ResultStateEnum PostOrSave(){
		ResultStateEnum state;
		if (postingTime == null
				|| postingTime.isBefore(DateTime.now())) {
			MakePost();
			postingTime = DateTime.now(DateTimeZone.UTC);
			result.postingTime = postingTime.getMillis();
			awaitingPostingTime = false;
			state = ResultStateEnum.posted;
		} else {
			awaitingPostingTime = true;
			result = Result.Success("Saved for later");
			state = ResultStateEnum.saved;
		}
		DAO.SaveThing(this, this.getParent());
		return state;
	}

	String subject;
	String contents;
	String siteName;
	long output;
	public Result result;
	@Index public DateTime postingTime;
	@Index public Boolean awaitingPostingTime;
}