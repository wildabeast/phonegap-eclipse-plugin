package com.phonegap.sdk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class PhoneGapBuildController {
	
	private String authUrl = "https://build.phonegap.com/api/v0/me";
	private String buildUrl = "https://build.phonegap.com/api/v0/apps";
	
	public PhoneGapBuildController() {
		
	}

	public boolean authenticate(final String username, final String password) {

	  try {
		  
		  String auth = new String(Base64.encodeBase64((username + ":" + password).getBytes()));
		  System.out.println(auth);
		  
		  URL url = new URL(authUrl);
		  URLConnection conn = url.openConnection();
		  
		  // this fails -- can't stop the auth window.
		  conn.setAllowUserInteraction(false);
		  conn.setRequestProperty("Authorization", "Basic " + auth);
		  
		  InputStream in = conn.getInputStream();
		  
		  InputStreamReader is=new InputStreamReader(in);
	      BufferedReader br=new BufferedReader(is);
	      String read=br.readLine();
	      while(read!=null){
	    	  System.out.println(read);
	           read=br.readLine();
	      }
		  
	  } catch (MalformedURLException e) {
		  e.printStackTrace();
	  } catch (IOException e) {
		  e.printStackTrace();
	  }

	  return false;
	}
	
	public boolean build(final String username, final String password, String appName, File appPackage) {
		
		try{
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("https://build.phonegap.com/api/v1/apps");
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
			httpclient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);
			
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	
			reqEntity.addPart("data", new StringBody("{ \"title\":\"From Java Client\", \"create_method\":\"file\" }"));
	
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
