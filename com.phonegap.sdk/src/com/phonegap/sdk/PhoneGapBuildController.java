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
		
	  try {
          
          String auth = new String(Base64.encodeBase64((username + ":" + password).getBytes()));
		  System.out.println(auth);
		  
		  URL url = new URL(buildUrl);
		  URLConnection conn = url.openConnection();
		  
		  // this fails -- can't stop the auth window.
		  conn.setAllowUserInteraction(false);
		  conn.setRequestProperty("Authorization", "Basic " + auth);
		  String boundary = Long.toHexString(System.currentTimeMillis());
		  conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		  conn.setDoOutput(true);
		  
		  System.out.println("\r\n***************************************\r\n");
          
		  BufferedOutputStream bos = new BufferedOutputStream( conn.getOutputStream() );
		  BufferedInputStream bis = new BufferedInputStream( new FileInputStream( appPackage ) );
		  String dataParam = "--" + boundary + "\r\n";
		  dataParam += "Content-Disposition: form-data; name=\"data\"\r\n";
		  dataParam += "{\"title\":\"" + appName + "\"}\r\n\r\n";
		  System.out.println(dataParam);
		  bos.write(dataParam.getBytes());
		  
		  String fileParam = "--" + boundary + "\r\n";
		  fileParam += "Content-Disposition: form-data; name=\"file\"; filename=\"" + appPackage.getName() + "\"\r\n";
		  fileParam += "Content-Type: application/octet-stream\r\n";
		  fileParam += "Content-Transfer-Encoding: binary\r\n";
		  System.out.println(fileParam);
		  
		  bos.write(fileParam.getBytes());
		  
		  int i;
          while ((i = bis.read()) != -1)
          {
             bos.write( i );
          }

          System.out.println("--" + boundary + "--\r\n");
          System.out.println("\r\n***************************************\r\n");
          bos.write(("--" + boundary + "--\r\n").getBytes());
          bos.flush();
          
          InputStream in;
          HttpURLConnection httpConn = (HttpURLConnection)conn;
          if (httpConn.getResponseCode() >= 400) {
        	  in = httpConn.getErrorStream();
          } else {
        	  in = httpConn.getInputStream();
          }
          BufferedReader br=new BufferedReader(new InputStreamReader(in));
          String read=br.readLine();
          while(read!=null){
        	  System.out.println(read);
        	  read=br.readLine();
          }

          bis.close();
          bos.close();
          
      
	  } catch (MalformedURLException e) {
		  e.printStackTrace();
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
	  
		
	  return false;
	}

}
