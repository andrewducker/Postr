package com.postr.DataTypes.Inputs;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.googlecode.objectify.annotation.Subclass;
import com.postr.BaseFeedParser;
import com.postr.FeedReader;
import com.postr.DataTypes.BaseSaveable;
import com.postr.DataTypes.LinkSet;

@Subclass(index=true)
public abstract class BaseInput extends BaseSaveable {
	protected String userName;
	
	protected BaseInput(){this.siteName = getSiteName();}

	BaseInput(String userName) {
		super();
		this.userName = userName;
		this.siteName = getSiteName();
	}
	
	//Needed for JSon
	@SuppressWarnings("unused")
	private String siteName;

	public abstract String getSiteName();
	
	public ListenableFuture<LinkSet> Read() throws Exception {

		ListenableFuture<HTTPResponse> feedContents = FeedReader.Read(GetFeedURL());
		
		return Futures.transform(feedContents, GetParser());
	}
		
	abstract BaseFeedParser GetParser();
	

	protected String GetURLForTag(String tag) {
		return null;
	}
	
	abstract String GetFeedURL();

}
