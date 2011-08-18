package com.phonegap.sdk.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class PhoneGapBuildSummaryPage extends WizardPage {
	
	protected PhoneGapBuildSummaryPage(String pageName) {
		super(pageName);
		setTitle("PhoneGap Build Summary");
		setDescription("Build completed, here's yo apps");
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
	}

}
