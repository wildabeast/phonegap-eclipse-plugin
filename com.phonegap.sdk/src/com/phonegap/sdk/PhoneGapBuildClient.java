package com.phonegap.sdk;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class PhoneGapBuildClient {
	
	private String AUTH_URL = "https://build.phonegap.com/api/v1/me";
	private String BUILD_URL = "https://build.phonegap.com/api/v1/apps";
	private String BUILD_STRING = "{ \"title\":\"%s\", \"create_method\":\"file\" }";
	private String ERROR_STRING = "{ \"error\": \"%s\" }";
	
	public PhoneGapBuildClient() {
		
	}

	public String authenticate(final String username, final String password) {
		try {
			  	DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(AUTH_URL);
				UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
				httpclient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);
	
				System.out.println("executing request " + httpget.getRequestLine());
				HttpResponse response;
				response = httpclient.execute(httpget);
	
				HttpEntity resEntity = response.getEntity();
		
				if (resEntity != null) {
					String page = EntityUtils.toString(resEntity);
					System.out.println("PAGE :" + page);
					System.out.println("status : " + response.getStatusLine().getStatusCode());
					return page;
				} else {
					return String.format(ERROR_STRING, "Could not get response");
				}
				
			}  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return String.format(ERROR_STRING, e.getMessage());
			}
	}
	
	public String build(final String username, final String password, String appName, File appPackage) {
		
		try{
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(BUILD_URL);
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
			httpclient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);
			
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	
			reqEntity.addPart("data", new StringBody(String.format(BUILD_STRING, appName)));
	
			FileBody bin = new FileBody(appPackage);
			reqEntity.addPart("file", bin );
	
			httppost.setEntity(reqEntity);
	
			System.out.println("executing request " + httppost.getRequestLine());
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
	
			if (resEntity != null) {
				String page = EntityUtils.toString(resEntity);
				System.out.println("PAGE :" + page);
				System.out.println("status : " + response.getStatusLine().getStatusCode());
				return page;
			} else {
				return String.format(ERROR_STRING, "Could not get response");
			}
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return String.format(ERROR_STRING, e.getMessage());
		}

	}

}
