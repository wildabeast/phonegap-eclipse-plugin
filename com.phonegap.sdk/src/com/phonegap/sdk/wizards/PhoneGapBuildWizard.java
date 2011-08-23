package com.phonegap.sdk.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;

import com.phonegap.sdk.Activator;
import com.phonegap.sdk.PhoneGapBuildController;

public class PhoneGapBuildWizard extends Wizard {

	IProject _project = null;
	PhoneGapBuildSummaryPage _summaryPage = null;
	PhoneGapBuildLoginPage _loginPage = null;
	PhoneGapBuildController buildController = null;
	
	
    public PhoneGapBuildWizard(IProject proj) {
    	_project = proj;
        setWindowTitle("PhoneGap Build");
        setNeedsProgressMonitor(true);
        setDefaultPageImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/buildbot_wizard_banner.png"));
    }
    
	public void addPages() {
		buildController = new PhoneGapBuildController(_project);
		
        _loginPage = new PhoneGapBuildLoginPage("loginPage", _project.getName(), buildController);
        addPage(_loginPage);
        
        _summaryPage = new PhoneGapBuildSummaryPage("PhoneGap Build Summary", buildController);
        addPage(_summaryPage);
	}
	
	@Override
	public boolean performFinish() {
		return true;
	}
	
	public boolean canPerformFinish() {
		return _loginPage.isPageComplete();
	}

}
