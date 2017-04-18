package com.user.info;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.file.dropbox.operations.auth.DropboxAuthenticator;

final public class UserInfo {

	private String userName;
	private String userRootDirectory;
	private DbxClient client = null;
	
	public UserInfo(String uName, String userRoot, String authKey) {
		this.userName = uName;
		this.userRootDirectory = userRoot;
		this.client = initDropbox(authKey);
	
	}

	private DbxClient initDropbox(String authKey) {
		try {
			return DropboxAuthenticator.authenticate(authKey, userName);
		} catch (DbxException e) {
			e.printStackTrace();
		}
		return null;
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
	
	
}
