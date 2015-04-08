package com.postr.DataTypes.Inputs;

import java.util.concurrent.Future;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.common.util.concurrent.Futures;
import com.googlecode.objectify.annotation.Subclass;
import com.postr.BaseFeedParser;
import com.postr.FeedReader;
import com.postr.DataTypes.BaseSaveable;
import com.postr.DataTypes.LinkSet;

@SuppressWarnings("serial")
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
	
	public LinkSet Read() throws Exception {

		HTTPResponse feedContents = FeedReader.Read(GetFeedURL());
		
		BaseFeedParser parser = GetParser();
		
		return parser.apply(feedContents);
	}
		
	public Future<LinkSet> ReadAsync() throws Exception {

		Future<HTTPResponse> feedContents = FeedReader.ReadAsync(GetFeedURL());
		
		return Futures.lazyTransform(feedContents, GetParser());
	}

	
	abstract BaseFeedParser GetParser();
	

	protected String GetURLForTag(String tag) {
		return null;
	}
	
	abstract String GetFeedURL();

}
