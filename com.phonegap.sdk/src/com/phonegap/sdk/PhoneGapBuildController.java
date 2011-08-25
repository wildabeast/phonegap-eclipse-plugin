package com.phonegap.sdk;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class PhoneGapBuildController {
	
	private String authUrl = "https://build.phonegap.com/api/v1/me";
	private String buildUrl = "https://build.phonegap.com/api/v1/apps";
	
	public PhoneGapBuildController() {
		
	}

	public boolean authenticate(final String username, final String password) {
		try {
			  	DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(authUrl);
				UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
				httpclient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);
	
				System.out.println("executing request " + httpget.getRequestLine());
				HttpResponse response;
				response = httpclient.execute(httpget);
	
				HttpEntity resEntity = response.getEntity();
		
				if (resEntity != null) {
					String page = EntityUtils.toString(resEntity);
					System.out.println("PAGE :" + page);
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  return false;
	}
	
	public boolean build(final String username, final String password, String appName, File appPackage) {
		
		try{
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(buildUrl);
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
			httpclient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);
			
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	
			reqEntity.addPart("data", new StringBody(String.format("{ \"title\":\"%s\", \"create_method\":\"file\" }", appName)));
	
			FileBody bin = new FileBody(appPackage);
			reqEntity.addPart("file", bin );
	
			httppost.setEntity(reqEntity);
	
			System.out.println("executing request " + httppost.getRequestLine());
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
	
			if (resEntity != null) {
				String page = EntityUtils.toString(resEntity);
				System.out.println("PAGE :" + page);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	  
		
	  return false;
	}

}
