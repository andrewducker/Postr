package com.postr.DataTypes.Outputs;
import java.util.Comparator;

import org.joda.time.DateTimeComparator;

import com.postr.DataTypes.Outputs.BasePost;


public class BasePostComparator implements Comparator<BasePost> {

	@Override
	public int compare(BasePost arg0, BasePost arg1) {
		return DateTimeComparator.getInstance().compare(arg0.getPostingTime(), arg1.getPostingTime());
	}
}
