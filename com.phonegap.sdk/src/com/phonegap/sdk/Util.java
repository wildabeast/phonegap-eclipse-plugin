package com.phonegap.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class Util {
	public static void copyDirectory(File srcPath, File dstPath) throws IOException{
		if (srcPath.isDirectory())
		{
			if (!dstPath.exists())
			{
				dstPath.mkdir();
			}

			String files[] = srcPath.list();
			for(int i = 0; i < files.length; i++)
			{
				copyDirectory(new File(srcPath, files[i]), new File(dstPath, files[i]));
			}
		}
		else
		{
			if(!srcPath.exists())
			{
				System.out.println("File or directory does not exist.");
			}
			else
			{
				InputStream in = new FileInputStream(srcPath);
		        OutputStream out = new FileOutputStream(dstPath);
    
				// Transfer bytes from in to out
		        byte[] buf = new byte[1024];
				int len;
		        while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
		        out.close();
			}
		}
		System.out.println("Directory copied.");
	}
	
	public static Image getImage(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, path).createImage();
	}
	
	public static boolean zipDirectory(File srcDir, File destFile, String[] ignoreList) {
		
		List<File> fileList = new ArrayList<File>();
		
		addFiles(srcDir, fileList, ignoreList);
		
		try {
			ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(destFile));
			for (File file : fileList) {
				if (!file.isDirectory()) {
					addFileToZip(file, zip, srcDir);
				}
			}
			zip.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static void addFiles(File srcDir, List<File> fileList, String[] ignoreList) {
		File[] files = srcDir.listFiles();
		for (File file : files) {
			boolean skip = false;
			for (String ignore : ignoreList) {
				if (file.getName().equals(ignore)) {
					skip = true;
					break;
				}
			}
			if (skip || file.getName().startsWith(".")) {
				continue;
			}
			fileList.add(file);
			if (file.isDirectory()) {
				addFiles(file, fileList, ignoreList);
			}
		}
	}
	
	public static void addFileToZip(File file, ZipOutputStream zip, File baseDir) {
		try {
			
			// create zip entry
			String filePath =  file.getCanonicalPath().substring(baseDir.getCanonicalPath().length() + 1, file.getCanonicalPath().length());
			zip.putNextEntry(new ZipEntry(filePath));
			
			// write file to zip
			FileInputStream inStream = new FileInputStream(file);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = inStream.read(bytes)) >= 0) {
				zip.write(bytes, 0, length);
			}
			
			inStream.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
