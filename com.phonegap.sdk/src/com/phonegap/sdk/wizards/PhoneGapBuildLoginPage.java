package com.phonegap.sdk.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class PhoneGapBuildLoginPage extends WizardPage {

	protected PhoneGapBuildLoginPage(String pageName) {
		super(pageName);
		setTitle("PhoneGap Build Authentication");
		setDescription("Authenticate yo' self.");
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

	    Label usernameLabel = new Label(composite, SWT.RIGHT);
	    usernameLabel.setText("Email: ");
	    usernameLabel.setLayoutData(labelGrid);

	    Text usernameField = new Text(composite, SWT.SINGLE | SWT.BORDER);
	    usernameField.setLayoutData(textGrid);

	    Label passwordLabel = new Label(composite, SWT.RIGHT);
	    passwordLabel.setText("Password: ");
	    passwordLabel.setLayoutData(labelGrid);

	    Text passwordField = new Text(composite, SWT.SINGLE | SWT.PASSWORD | SWT.BORDER);
	    passwordField.setLayoutData(textGrid);
		
		setControl(composite);
	}

}
