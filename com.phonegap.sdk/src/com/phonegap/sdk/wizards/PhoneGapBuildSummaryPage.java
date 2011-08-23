package com.phonegap.sdk.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.phonegap.sdk.PhoneGapBuildController;

public class PhoneGapBuildSummaryPage extends WizardPage implements Listener {
	
	private String _email;
	private String _password;
	private Button buildButton;
	private PhoneGapBuildController buildController;
	
	protected PhoneGapBuildSummaryPage(String pageName, PhoneGapBuildController buildController) {
		super(pageName);
		setTitle("PhoneGap Build Summary");
		setDescription("Build completed, here's yo apps");
		this.buildController = buildController;
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
			buildController.build(getContainer(), _email, _password);
		}
		
	}

}
