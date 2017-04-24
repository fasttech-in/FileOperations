package com.file.abstracts;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;

import com.dropbox.core.DbxException;
import com.file.Interfaces.IFileOperations;
import com.user.info.UserInfo;

public abstract class Operations implements IFileOperations {

	protected UserInfo userInfo;
	
	public Operations(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public boolean upload(String fromPath, String toPath) throws IOException, DbxException {

		initializeUploadProcess(fromPath, toPath);
		boolean isUploaded = uploadProcess(fromPath, toPath);
		if (isUploaded)
			uploadDropboxProcess(fromPath, toPath);

		return isUploaded;
	}

	@Override
	public String download(String fromPath, String toPath) throws IOException {

		return downloadProcess(fromPath, toPath);
	}

	@Override
	public List<String> search(String searchStr, String searchFolder) {
		List<String> matchPaths = new ArrayList<String>();
		File folder = new File(searchFolder);
		matchPaths = search(matchPaths, searchStr, folder);
		
		return matchPaths;
	}

	private List<String> search(List<String> matchPaths, String searchStr,
			File file) {
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				search(matchPaths, searchStr, files[i].getAbsoluteFile());
			}
		} else {
			if(file.getName().contains(searchStr)){
				matchPaths.add(file.getAbsolutePath());
			}
		}
		
		return matchPaths;
	}

	@Override
	public String zip(String fromPath, String toPath) throws IOException, ZipException {
		
		return zipProcess(fromPath, toPath);
	}

	@Override
	public boolean openFile(String filePath) throws IOException, InterruptedException {
		File file = FileUtils.getFile(filePath);
		if (file.exists()) {
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(file);
			} else {
				Process p = Runtime.getRuntime().exec(
						"rundll32 url.dll, FileProtocolHandler " + filePath);
				p.waitFor();
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean deleteFile(String filePath) {
		File file = FileUtils.getFile(filePath);
		return FileUtils.deleteQuietly(file);
	}
	
	public String getUserRootDirectory() {
		return userInfo.getUserRootDirectory();
	}
	
	protected String convertToDropboxPath(String uploadPath) {
		if(uploadPath.contains("/")){
			uploadPath = uploadPath.replaceAll("\\/", "\\\\");
		}
		return "/DMS/Data/" + userInfo.getUserName() + "/"
				+ uploadPath.replace(userInfo.getUserRootDirectory(), "").replaceAll("\\\\","/");
	}

	abstract protected void initializeUploadProcess(String fromPath,
			String toPath);

	abstract protected boolean uploadProcess(String fromPath, String toPath) throws IOException;

	abstract protected boolean uploadDropboxProcess(String fromPath,
			String toPath) throws DbxException, IOException;
	
	abstract protected String downloadProcess(String fromPath, String toPath) throws IOException;
	
	abstract protected String zipProcess(String fromPath, String toPath) throws IOException, ZipException;
	
}
