package com.phonegap.sdk.wizards;

import java.net.URI;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import com.phonegap.sdk.Activator;
import com.phonegap.sdk.projects.PhoneGapProject;

public class NewPhoneGapProjectWizard extends Wizard implements INewWizard, IExecutableExtension {
	
	private WizardNewProjectCreationPage detailsPage;
	private IConfigurationElement config;


	public NewPhoneGapProjectWizard() {
		setWindowTitle("New PhoneGap Project");
        setDefaultPageImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/phonegap_wizard_banner.jpg"));
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void addPages() {
		super.addPages();
		
		detailsPage = new WizardNewProjectCreationPage("PhoneGap Project Details");
		detailsPage.setTitle("PhoneGap Project Details");
		detailsPage.setDescription("Define the properties of your PhoneGap project here.");
		addPage(detailsPage);
	}

	@Override
	public boolean performFinish() {
		
		URI location = null;
		if (!detailsPage.useDefaults()) {
			location = detailsPage.getLocationURI();
		}
		
		PhoneGapProject.createProject(detailsPage.getProjectName(), location);
		
		BasicNewProjectResourceWizard.updatePerspective(config);

		
		return true;
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		this.config = config;
	}

}
