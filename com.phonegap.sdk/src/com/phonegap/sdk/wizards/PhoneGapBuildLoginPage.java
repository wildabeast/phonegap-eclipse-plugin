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
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.phonegap.sdk.PhoneGapBuildClient;


public class PhoneGapBuildLoginPage extends WizardPage implements Listener {
	
	private static String TEST_CONNECTION_SUCCESS = "Yo' credentials worked";
	private static String EMAIL_REGEX = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	
	private Text emailField;
	private Text passwordField;
	private Button testConnButton;
	private Label testLabel;
	private PhoneGapBuildClient buildController;

	protected PhoneGapBuildLoginPage(String pageName, PhoneGapBuildClient buildController, String projName) {
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
	    
	    testLabel = new Label(composite, SWT.CENTER);
	    testLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false , 2, 1));

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
			final Display disp = Display.getCurrent();
			getWizard().getContainer().run(true, true, new IRunnableWithProgress() {
		      public void run(IProgressMonitor monitor) {
	    		  monitor.beginTask("Authenticating: ", IProgressMonitor.UNKNOWN);
		    	  final String authResponse = buildController.authenticate(email, password);
		    	  disp.syncExec(new Runnable() {
		    				    public void run(){
		    				    	JSONObject obj=  (JSONObject) JSONValue.parse(authResponse);
		    				    	String error = (String) obj.get("error");
		    				    	if (error != null) {
		    				    		setErrorMessage(error);
		    				    	} else {
		    				    		setMessage(TEST_CONNECTION_SUCCESS);
		    				    	}
		    				    }
		    				  });

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
		
		Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);  
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
