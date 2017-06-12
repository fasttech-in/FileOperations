package com.user.info;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.file.dropbox.operations.auth.DropboxAuthenticator;

final public class UserInfo {

	private String userName;
	private String userRootDirectory;
	private DbxClient client = null;
	private String userDropboxRoot;
	private String authorizationKey;
	private String dropboxConstantPath ="/DMS/Data/";
	
	public UserInfo(String uName, String userRoot, String authKey) {
		this.userName = uName;
		this.userRootDirectory = userRoot;
		this.authorizationKey = authKey;
		this.client = initDropbox(authKey);
		if(isDropboxSupported()) {
			userDropboxRoot = dropboxConstantPath+userName;
		}
	}

	private DbxClient initDropbox(String authKey) {
		try {
			return DropboxAuthenticator.authenticate(authKey, userName);
		} catch (DbxException e) {
			System.out.println("Clound DB access denied. : "+e.getMessage());
		}
		return null;
	}
	
	public boolean reconnectDropbox(){
		this.client = initDropbox(authorizationKey);
		if(isDropboxSupported()) {
			userDropboxRoot = "/DMS/Data/"+userName;
		}
		return isDropboxSupported();
	}
	
	public boolean isDropboxSupported() {
		return client !=null;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserRootDirectory() {
		return userRootDirectory;
	}

	public void setUserRootDirectory(String userRootDirectory) {
		this.userRootDirectory = userRootDirectory;
	}

	public DbxClient getClient() {
		return client;
	}

	public void setClient(DbxClient client) {
		this.client = client;
	}

	public String getUserDropboxRoot() {
		return userDropboxRoot;
	}
	
	public String getUserDisplayPath(String dpbPath) {
		if(dpbPath.contains(dropboxConstantPath)){ 
			dpbPath = dpbPath.replace(dropboxConstantPath, "\\");
		}
		return dpbPath;
	}
	
}
