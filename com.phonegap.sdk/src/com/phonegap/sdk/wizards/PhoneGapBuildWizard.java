package com.phonegap.sdk.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;

import com.phonegap.sdk.Activator;
import com.phonegap.sdk.PhoneGapBuildClient;

public class PhoneGapBuildWizard extends Wizard {

	IProject _project = null;
	PhoneGapBuildSummaryPage _summaryPage = null;
	PhoneGapBuildLoginPage _loginPage = null;
	PhoneGapBuildClient buildController = null;
	
	
    public PhoneGapBuildWizard(IProject proj) {
    	_project = proj;
        setWindowTitle("PhoneGap Build");
        setNeedsProgressMonitor(true);
        setDefaultPageImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/buildbot_wizard_banner.png"));
    }
    
	public void addPages() {
		buildController = new PhoneGapBuildClient();
		
        _loginPage = new PhoneGapBuildLoginPage("loginPage", buildController, _project.getName());
        addPage(_loginPage);
        
        _summaryPage = new PhoneGapBuildSummaryPage("PhoneGap Build Summary", buildController, _project);
        addPage(_summaryPage);
	}
	
	@Override
	public boolean performFinish() {
		return true;
	}

}
