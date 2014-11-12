package com.sean.http.core;

import java.util.List;

import org.apache.http.NameValuePair;

public interface HttpListener
{
	public void onError(Request request, Exception e);
	
	public void onPreRequest(Request request, List<NameValuePair> Defparam);
}
