package com.phonegap.sdk.wizards;

import org.eclipse.jface.wizard.Wizard;

import com.phonegap.sdk.Activator;

public class PhoneGapBuildWizard extends Wizard {

    public PhoneGapBuildWizard() {
        setWindowTitle("PhoneGap Build");
        setNeedsProgressMonitor(true);
        setDefaultPageImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/buildbot_wizard_banner.png"));
    }
    
	public void addPages() {
        PhoneGapBuildLoginPage loginPage = new PhoneGapBuildLoginPage("PhoneGap Build Login");
        addPage(loginPage);
        
        PhoneGapBuildSummaryPage summaryPage = new PhoneGapBuildSummaryPage("PhoneGap Build Summary");
        addPage(summaryPage);
	}
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return true;
	}

}
