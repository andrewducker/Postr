package com.postr;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Future;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.common.util.concurrent.JdkFutureAdapters;
import com.google.common.util.concurrent.ListenableFuture;

public class FeedReader {

	public static ListenableFuture<HTTPResponse> Read(String feedURL) throws MalformedURLException
	{
		URL url = new URL(feedURL);
		HTTPRequest httpRequest = new HTTPRequest(url);
		HTTPHeader header = new HTTPHeader("Cache-Control", "max-age=300");
		httpRequest.setHeader(header);
		
		URLFetchService fetchService = URLFetchServiceFactory.getURLFetchService();
		
		Future<HTTPResponse> response =  fetchService.fetchAsync(httpRequest);
		return JdkFutureAdapters.listenInPoolThread(response);
	}
}
