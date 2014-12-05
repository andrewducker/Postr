package com.postr.DataTypes;

import java.util.List;
import java.util.Vector;

import org.joda.time.DateTime;


public class LinkEntry implements Comparable<LinkEntry> {

	public String URL;
	public List<LinkTag> Tags = new Vector<LinkTag>();
	public String Title;
	public String Description;
	public DateTime PostedDate;
	@Override
	public int compareTo(LinkEntry arg0) {
		return PostedDate.compareTo(arg0.PostedDate);
		
	}
	public String getURL() {
		return URL;
	}
	public List<LinkTag> getTags() {
		return Tags;
	}
	public String getTitle() {
		return Title;
	}
	public String getDescription() {
		return Description;
	}
	public DateTime getPostedDate() {
		return PostedDate;
	}
	
	
}
