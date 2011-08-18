package com.phonegap.sdk.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionDelegate;

import com.phonegap.sdk.wizards.PhoneGapBuildWizard;

public class PhoneGapBuildAction extends ActionDelegate {
	
	//private ISelection _selection;
	//private IWorkbenchPart _part;

	public PhoneGapBuildAction() {
		// TODO Auto-generated constructor stub
	}
	
	public void run(IAction action) {
		PhoneGapBuildWizard wizard = new PhoneGapBuildWizard();
		WizardDialog dialog = new WizardDialog(wizard.getShell(), wizard);
		dialog.create();
		dialog.open();
		
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		//this._selection = selection;
	}
	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		//this._part = targetPart;
	}
	
}
