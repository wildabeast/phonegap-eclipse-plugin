package com.phonegap.sdk.projects;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

import com.phonegap.sdk.Util;
import com.phonegap.sdk.natures.PhoneGapProjectNature;

public class PhoneGapProject {

	public static IProject createProject(String name, URI location) {
		Assert.isNotNull(name);
		
		IProject project = createEclipseProject(name, location);
		try {
			addNature(project);
			createProjectFiles(project);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return project;
		
	}

	private static IProject createEclipseProject(String name, URI location) {
		
		IProject newProj = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		
		if (!newProj.exists()) {
			IProjectDescription desc = newProj.getWorkspace().newProjectDescription(newProj.getName());
			
			if (location != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location)) {
				location = null;
			}
			desc.setLocationURI(location);

			try {
				newProj.create(desc, null);
				if (!newProj.isOpen()) {
					newProj.open(null);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		return newProj;
	}
	
	private static void createProjectFiles(IProject project) throws CoreException {

		File projDir = new File(project.getLocationURI().getPath());
		try {
			File template = new File(FileLocator.toFileURL(Platform.getBundle("com.phonegap.sdk").getEntry("templates/basic")).getFile());
			Util.copyDirectory(template, projDir);
			project.refreshLocal(IProject.DEPTH_INFINITE, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void addNature(IProject project) throws CoreException {
		if (!project.hasNature(PhoneGapProjectNature.NATURE_ID)) {
			IProjectDescription description = project.getDescription();
			String[] prevNatures = description.getNatureIds();
			String[] newNatures = new String[prevNatures.length+1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = PhoneGapProjectNature.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
		}
	}
	
}
