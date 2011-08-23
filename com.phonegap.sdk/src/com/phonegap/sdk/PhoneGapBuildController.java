package com.phonegap.sdk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.apache.commons.codec.binary.Base64;

public class PhoneGapBuildController {
	
	private IProject _project;
	public PhoneGapBuildController(IProject proj) {
		this._project = proj;
	}

	public boolean authenticate(IRunnableContext runner, final String username, final String password) {
		try {
			runner.run(true, true, new IRunnableWithProgress() {
			      public void run(IProgressMonitor monitor) {
			    	  try {
			    		  
			    		  String auth = new String(Base64.encodeBase64((username + ":" + password).getBytes()));
			    		  log(auth);
			    		  
			    		  URL url = new URL("https://build.phonegap.com/api/v0/me");
			    		  URLConnection conn = url.openConnection();
			    		  
			    		  // this fails -- can't stop the auth window.
			    		  conn.setAllowUserInteraction(false);
			    		  conn.setRequestProperty("Authorization", "Basic " + auth);
			    		  
			    		  monitor.beginTask("Authenticating: ", IProgressMonitor.UNKNOWN);
			    		  InputStream in = conn.getInputStream();
			    		  
			    		  InputStreamReader is=new InputStreamReader(in);
		                  BufferedReader br=new BufferedReader(is);
		                  String read=br.readLine();
		                  while(read!=null){
		                	  System.out.println(read);
		                       read=br.readLine();
		                  }
			    		  
			    	  } catch (Exception e) {
			    		  log(e.toString());
			    	  } finally {
			    		  monitor.done();
			    	  }

			      }
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	
	final static String[] ignoreList = { ".project" };
	
	public boolean build(IRunnableContext runner, final String username, final String password) {
		// zip project to a temp directory
		try {
			runner.run(true, true, new IRunnableWithProgress() {
			      public void run(IProgressMonitor monitor) {
			    	  monitor.beginTask(String.format("Packaging %s", _project.getName()), IProgressMonitor.UNKNOWN);

			    	  IPath dest = Activator.getDefault().getStateLocation();
			    	  File destFile = dest.append(_project.getName()).addFileExtension("zip").toFile();
			    	  
			    	  try {
			    	  
				    	  File srcDir = _project.getLocation().toFile();
				          Util.zipDirectory(srcDir, destFile, ignoreList);
				          
				          String auth = new String(Base64.encodeBase64((username + ":" + password).getBytes()));
			    		  log(auth);
			    		  
			    		  URL url;
			    		  URLConnection conn;
			    		  url = new URL("https://build.phonegap.com/api/v0/apps");
			    		  conn = url.openConnection();
			    		  
			    		  // this fails -- can't stop the auth window.
			    		  conn.setAllowUserInteraction(false);
			    		  conn.setRequestProperty("Authorization", "Basic " + auth);
			    		  String boundary = Long.toHexString(System.currentTimeMillis());
			    		  conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			    		  conn.setDoOutput(true);
			    		  
			    		  monitor.beginTask("Authenticating: ", IProgressMonitor.UNKNOWN);
				          
			    		  BufferedOutputStream bos = new BufferedOutputStream( conn.getOutputStream() );
			    		  BufferedInputStream bis = new BufferedInputStream( new FileInputStream( destFile ) );
			    		  bos.write("data={'title':'Wammy'}\r\n".getBytes());
			    		  
			    		  int i;
			              // read byte by byte until end of stream
			              while ((i = bis.read()) != -1)
			              {
			                 bos.write( i );
			              }
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
			    	  
			    	  if (destFile.exists()) {
			        	  destFile.delete();
			          }
			    	  
			          monitor.done();
			      }
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// upload
		// delete
		
		
		return false;
	}
	
	public static void log(String text) {
		Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, text));
	}

}
