package com.file.operations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.FileUtils;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;
import com.file.abstracts.Operations;
import com.user.info.UserInfo;

public class FileOperations extends Operations {

	public FileOperations(UserInfo userInfo) {
		super(userInfo);
	}

	@Override
	protected void initializeUploadProcess(String fromPath, String toPath) {

	}

	@Override
	protected boolean uploadProcess(String fromPath, String toPath)
			throws IOException {
		File fromFile = FileUtils.getFile(fromPath);
		File toFile = FileUtils.getFile(toPath);
		if(!toFile.exists()) {
			toFile.mkdirs();
		}
		if (fromFile != null && fromFile.isDirectory()) {
			FileUtils.copyDirectory(fromFile, toFile, false);
			return true;
		} else if (fromFile != null && fromFile.isFile()) {
			FileUtils.copyFileToDirectory(fromFile, toFile, false);
			return true;
		}
		return false;
	}

	@Override
	protected boolean uploadDropboxProcess(String fromPath, String toPath) 
			throws DbxException, IOException {
		if (userInfo.isDropboxSupported()) {
			File file = new File(fromPath);
			if (file.isDirectory()) {
				if (file.listFiles().length > 0) {
					File[] files = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						if(files[i].getAbsoluteFile().isFile()) {
							uploadingFiles(userInfo.getClient(), files[i].getAbsoluteFile(), toPath);
						} else {
							uploadDropboxProcess(files[i].getAbsolutePath(), toPath+"/"+files[i].getName());
						}
					}
				}
			} else if(file.isFile()) {
				uploadingFiles(userInfo.getClient(), file, toPath);
			}
 		}

		return false;
	}

	private void uploadingFiles(DbxClient client, File inputFile,
			String uploadPath) throws DbxException, IOException {
		// File inputFile = new File(filePath);
		// inputFile.createNewFile();
		FileInputStream inputStream=null;
		try {
			uploadPath = convertToDropboxPath(uploadPath);
			inputStream = new FileInputStream(inputFile);
			
			DbxEntry.File uploadedFile = client.uploadFile(uploadPath + "/"
					+ inputFile.getName(), DbxWriteMode.add(),
					inputFile.length(), inputStream);
			System.out.println("Uploaded: " + uploadedFile.toString());
		} finally {
			if (inputStream != null)
				inputStream.close();
		}
	}

	@Override
	protected String downloadProcess(String fromPath, String toPath)
			throws IOException {
		return uploadProcess(fromPath, toPath)?toPath:null;
	}

	@Override
	protected String zipProcess(String fromPath, String toPath) throws IOException, ZipException {
		File fromFile = FileUtils.getFile(fromPath);
		File toFile = FileUtils.getFile(toPath);
		File f = new File(toFile.getAbsolutePath(), fromFile.getName()+".zip");

		ZipParameters params = new ZipParameters();
		params.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		params.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		
		ZipFile zipper = new ZipFile(f.getAbsolutePath());
		if(fromFile.isDirectory()) { 
			zipper.addFolder(fromFile, params);
		} else {
			zipper.addFile(fromFile, params);
		}
		
		return f.getAbsolutePath();
	}

}
