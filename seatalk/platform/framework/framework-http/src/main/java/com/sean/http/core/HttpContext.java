package com.sean.http.core;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;

/**
 * http context
 * @author sean
 */
public class HttpContext
{
	public static String Hostname = "localhost";
	public static int Port = 80;
	public static String AppName = "demo";
	public static String Version = "v1";
	public static HttpListener listener = null;
	
	public static List<NameValuePair> Defparam = new LinkedList<NameValuePair>();
}
