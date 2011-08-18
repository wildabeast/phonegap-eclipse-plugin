package com.phonegap.sdk;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

public class PhoneGapBuildController extends Job {
	
	protected PhoneGapBuildController(String jobName, String username, String password) {
		super(jobName);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return null;
	}

}
