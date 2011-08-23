package com.phonegap.sdk.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import com.phonegap.sdk.PhoneGapBuildController;


public class PhoneGapBuildLoginPage extends WizardPage implements Listener {
	
	private Text emailField;
	private Text passwordField;
	private Button testConnButton;
	private PhoneGapBuildController buildController;
	private static String emailPattern = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

	protected PhoneGapBuildLoginPage(String pageName, PhoneGapBuildController buildController, String projName) {
		super(pageName);
		setTitle("Build Project: " + projName);
		setDescription("Authenticate yo' self.");
		this.buildController = buildController;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		
		// Setup layout
		GridLayout layout = new GridLayout();
	    layout.numColumns = 2;
	    layout.verticalSpacing = 10;
	    composite.setLayout(layout);
	    
	    GridData labelGrid = new GridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1);
	    GridData textGrid = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
	    textGrid.widthHint = 200;

	    Label emailLabel = new Label(composite, SWT.RIGHT);
	    emailLabel.setText("Email: ");
	    emailLabel.setLayoutData(labelGrid);

	    emailField = new Text(composite, SWT.SINGLE | SWT.BORDER);
	    emailField.setLayoutData(textGrid);
	    emailField.addListener(SWT.CHANGED, this);

	    Label passwordLabel = new Label(composite, SWT.RIGHT);
	    passwordLabel.setText("Password: ");
	    passwordLabel.setLayoutData(labelGrid);

	    passwordField = new Text(composite, SWT.SINGLE | SWT.PASSWORD | SWT.BORDER);
	    passwordField.setLayoutData(textGrid);
	    passwordField.addListener(SWT.CHANGED, this);
	    
	    testConnButton = new Button(composite, SWT.CENTER);
	    testConnButton.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false , 2, 1));
	    testConnButton.setText("Test Connection");
	    testConnButton.addListener(SWT.Selection, this);

		setControl(composite);
		setPageComplete(false);
	}

	@Override
	public void handleEvent(Event event) {
		if (event.widget.equals(emailField) || event.widget.equals(passwordField)) {
			if (!validateEmail(emailField.getText())) {
				setErrorMessage("Invalid email");
				setPageComplete(false);
			} else if (passwordField.getText().equals("")) {
				setErrorMessage("Enter a password");
				setPageComplete(false);
			} else {
				setErrorMessage(null);
				setPageComplete(true);
			}
		} else if (event.widget.equals(testConnButton)) {
			testConnection();
		}
		
	}
	
	private void testConnection() {
		try {
			final String email = emailField.getText();
			final String password = passwordField.getText();
			getWizard().getContainer().run(true, true, new IRunnableWithProgress() {
		      public void run(IProgressMonitor monitor) {
	    		  monitor.beginTask("Authenticating: ", IProgressMonitor.UNKNOWN);
		    	  buildController.authenticate(email, password);
		    	  monitor.done();
		      }
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean validateEmail(String email){
		Boolean isValid = false;
		CharSequence inputStr = email;  
		
		Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);  
		Matcher matcher = pattern.matcher(inputStr);  
		if(matcher.matches()){  
			isValid = true;  
		}  
		return isValid;  
	}
	
	public String getEmail() {
		return this.emailField.getText();
	}
	
	public String getPassword() {
		return this.passwordField.getText();
	}
}
