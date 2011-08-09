package com.phonegap.sdk.test;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.CoreException;
import org.junit.Assert;
import org.junit.Test;

import com.phonegap.sdk.projects.PhoneGapProject;

public class PhoneGapProjectTest {
	
    @SuppressWarnings("nls")
    
    @Test
    public void testCreateBadProject() {
        URI location = null;
        try {
            PhoneGapProject.createProject("", location);
            Assert.fail("Shouldn't be able to create a project with an empty string name"); //$NON-NLS-1$
        } catch (AssertionFailedException e) {
            // An exception was thrown as expected; the test passed.
        }
    }
    
    @Test
    public void testProjectSettingsFileExists() throws CoreException {
    	String name = "Test Project";
    	String projectPath = "/Users/ryanw/repo/workspaces/junit-workspace/" + name;

        IProject project = PhoneGapProject.createProject(name, null);

        String projectFilePath = projectPath + "/" + ".project";

        Assert.assertNotNull(project);
        assertFileExists(projectFilePath);
        assertTemplateDirsExist(projectPath);

        project.delete(true, null);
    }

    @SuppressWarnings("nls")
    private void assertTemplateDirsExist(String projectPath) {
        String[] paths = { "assets/css", "assets/js", "assets/img", "test" };
        for (String path : paths) {
            File file = new File(projectPath + "/" + path);
            if (!file.exists()) {
                Assert.fail("Directory " + path + " was not created.");
            }
        }
    }

    private void assertFileExists(String projectFilePath) {
        File file = new File(projectFilePath);

        if (!file.exists()) {
            Assert.fail("File " + projectFilePath + " does not exist."); //$NON-NLS-1$//$NON-NLS-2$
        }
    }

}
