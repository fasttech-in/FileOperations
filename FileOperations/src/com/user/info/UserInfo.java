package com.user.info;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.file.dropbox.operations.auth.DropboxAuthenticator;
import com.file.ui.PopupNotification;

final public class UserInfo {

	private String userName;
	private String userRootDirectory;
	private DbxClient client = null;
	private String userDropboxRoot;
	private String authorizationKey;
	private String dropboxConstantPath ="/DMS/Data/";
	private UserVO userVO;
	
	public UserInfo(UserVO vo) {
		userVO = vo;
		init(vo.getUserName(), vo.getUserRootDirectory(), vo.getUserAccessKey());
	}

	protected void init(String uName, String userRoot, String authKey) {
		this.userName = uName;
		this.userRootDirectory = userRoot;
		this.authorizationKey = authKey;
		this.client = initDropbox(authKey);
		if(isDropboxSupported()) {
			userDropboxRoot = dropboxConstantPath+userName;
		}
	}

	private DbxClient initDropbox(String authKey) {
		if(userVO.isServerService()) {
			try {
				return DropboxAuthenticator.authenticate(authKey, userName);
			} catch (DbxException e) {
				System.out.println("Cloud DB access denied. : "+e.getMessage());
			}
		} 
		return null;
	}
	
	public boolean reconnectDropbox() {
		if(userVO.isServerService()) {
			this.client = initDropbox(authorizationKey);
			if(isDropboxSupported()) {
				userDropboxRoot = dropboxConstantPath+userName;
			}
		} else {
			PopupNotification.showError("Cloud - Service", "Cloud service is disabled.");
		}
		return isDropboxSupported();
	}
	
	public UserVO getUserVO() {
		return userVO;
	}

	public void setUserVO(UserVO uservo) {
		this.userVO = uservo;
	}

	public boolean isDropboxSupported() {
		return client !=null && userVO.isServerService();
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
		return userVO.isServerService()?client:null;
	}

	public void setClient(DbxClient client) {
		this.client = client;
	}

	public String getUserDropboxRoot() {
		return userDropboxRoot;
	}
	
	public String getUserDisplayPath(String dpbPath) {
		if(dpbPath.contains(dropboxConstantPath)){ 
			dpbPath = dpbPath.replace(dropboxConstantPath, "/");
		}
		return dpbPath;
	}
	
}
