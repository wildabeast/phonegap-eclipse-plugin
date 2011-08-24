package com.phonegap.sdk.wizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.phonegap.sdk.Activator;
import com.phonegap.sdk.PhoneGapBuildController;
import com.phonegap.sdk.Util;

public class PhoneGapBuildSummaryPage extends WizardPage implements Listener {
	
	private String _email;
	private String _password;
	private Button buildButton;
	private PhoneGapBuildController buildController;
	final IProject _project;
	final static String[] ignoreList = { ".project" };
	
	protected PhoneGapBuildSummaryPage(String pageName, PhoneGapBuildController buildController, IProject project) {
		super(pageName);
		setTitle("PhoneGap Build Summary");
		setDescription("Build completed, here's yo apps");
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
	    layout.numColumns = 2;
	    layout.verticalSpacing = 10;
	    composite.setLayout(layout);
		
	    buildButton = new Button(composite, SWT.CENTER);
	    buildButton.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false , 2, 1));
	    buildButton.setText("Build");
	    buildButton.addListener(SWT.Selection, this);
		
		setControl(composite);
	}
	
	public boolean isPageComplete() {
		return false;
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		if (event.widget.equals(buildButton)) {
			getLoginFields();
			try {
				getWizard().getContainer().run(true, true, new IRunnableWithProgress() {
				      public void run(IProgressMonitor monitor) {
				    	  monitor.beginTask("Packaging", IProgressMonitor.UNKNOWN);
				    	  IPath dest = Activator.getDefault().getStateLocation();
				    	  File destFile = dest.append("phonegap.build").addFileExtension("zip").toFile();
				    	  
				    	  File srcDir = _project.getLocation().toFile();
				          Util.zipDirectory(srcDir, destFile, ignoreList);
				    	  buildController.build(_email, _password, _project.getName(), destFile);
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
		
	}

}
