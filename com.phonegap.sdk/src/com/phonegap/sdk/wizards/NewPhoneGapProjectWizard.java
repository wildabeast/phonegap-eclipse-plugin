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

import com.phonegap.sdk.projects.PhoneGapProject;

public class NewPhoneGapProjectWizard extends Wizard implements INewWizard, IExecutableExtension {
	
	private WizardNewProjectCreationPage _pageOne;
	private IConfigurationElement _configurationElement;


	public NewPhoneGapProjectWizard() {
		setWindowTitle("New PhoneGap Project");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void addPages() {
		super.addPages();
		
		_pageOne = new WizardNewProjectCreationPage("PhoneGap Project Details");
		_pageOne.setTitle("PhoneGap Project Details");
		_pageOne.setDescription("Define the properties of your PhoneGap project here.");
		addPage(_pageOne);
	}

	@Override
	public boolean performFinish() {
		
		URI location = null;
		if (!_pageOne.useDefaults()) {
			location = _pageOne.getLocationURI();
		}
		
		PhoneGapProject.createProject(_pageOne.getProjectName(), location);
		
		BasicNewProjectResourceWizard.updatePerspective(_configurationElement);

		
		return true;
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		// TODO Auto-generated method stub
		_configurationElement = config;
	}

}
