package com.phonegap.sdk.wizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLEncoder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.phonegap.sdk.Activator;
import com.phonegap.sdk.PhoneGapBuildClient;
import com.phonegap.sdk.Util;

public class PhoneGapBuildSummaryPage extends WizardPage implements Listener {
	
	private static String BUILD_SUCCESS_MESSAGE = "Submission successful";
	private static String PHONEGAP_BUILD_URL = "https://%s:%s@build.phonegap.com";
	
	private String _email;
	private String _password;
	private Button buildButton;
	private Link buildLink;
	private PhoneGapBuildClient buildController;
	final IProject _project;
	final static String[] ignoreList = { ".project" };
	private boolean buildComplete = false;
	
	protected PhoneGapBuildSummaryPage(String pageName, PhoneGapBuildClient buildController, IProject project) {
		super(pageName);
		setTitle("PhoneGap Build Summary");
		setDescription("Click Build to send your app to PhoneGap Build");
		this.buildController = buildController;
		this._project = project;
	}
	
	private void getLoginFields() {
		PhoneGapBuildLoginPage loginPage = (PhoneGapBuildLoginPage) this.getWizard().getPage("loginPage");
		_email = loginPage.getEmail();
		_password = loginPage.getPassword();
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		
		// Setup layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 10;
	    composite.setLayout(layout);
		
	    buildButton = new Button(composite, SWT.CENTER);
	    buildButton.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false , 1, 1));
	    buildButton.setText("Build");
	    buildButton.addListener(SWT.Selection, this);
	    
	    buildLink = new Link(composite, SWT.NONE);
	    buildLink.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false , 1, 1));
	    buildLink.setText("Success! Your apps are building. Go to <a>build.phonegap.com</a> to download your built apps.");
	    buildLink.addListener(SWT.Selection, this);
	    buildLink.setVisible(false);
		
		setControl(composite);
	}
	
	public boolean isPageComplete() {
		return buildComplete;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.widget.equals(buildLink)) {
			getLoginFields();
			try {
				// in-url basic auth doesn't seem to work in most browsers
				URL url = new URL(String.format(PHONEGAP_BUILD_URL, URLEncoder.encode(_email, "UTF-8"), URLEncoder.encode(_password, "UTF-8")));
				PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} else if (event.widget.equals(buildButton)) {
			buildLink.setVisible(false);
			getLoginFields();
			try {
				final Display disp = Display.getCurrent();
				getWizard().getContainer().run(true, true, new IRunnableWithProgress() {
				      public void run(IProgressMonitor monitor) {
				    	  monitor.beginTask("Packaging", IProgressMonitor.UNKNOWN);
				    	  IPath dest = Activator.getDefault().getStateLocation();
				    	  File destFile = dest.append("phonegap.build").addFileExtension("zip").toFile();
				    	  
				    	  File srcDir = _project.getLocation().toFile();
				          Util.zipDirectory(srcDir, destFile, ignoreList);
				    	  final String buildResp = buildController.build(_email, _password, _project.getName(), destFile);
				    	  System.out.println(buildResp);
				    	  disp.syncExec(new Runnable() {
		    				    public void run(){
		    				    	JSONObject jsonObj=(JSONObject)JSONValue.parse(buildResp);
		    				    	String error = jsonObj.get("error").toString();
		    				    	if (error.trim().equals("{}")) {
		    				    		setMessage(BUILD_SUCCESS_MESSAGE);
		    				    		buildLink.setVisible(true);
		    				    		buildComplete = true;
		    				    	} else {
		    				    		setErrorMessage(jsonObj.get("error").toString());
		    				    		buildLink.setVisible(false);
		    				    		buildComplete = false;
		    				    	}
		    				    }
		    				  });
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
		}
		setPageComplete(buildComplete);
		
	}

}
