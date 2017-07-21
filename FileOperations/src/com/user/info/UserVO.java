package com.user.info;

import javax.xml.bind.annotation.XmlRootElement;

import com.file.util.CommanUtil;

/**
 * 
 * @author sdahake
 *
 */
@XmlRootElement
public class UserVO {
	
	private String userName;
	private String password;
	private String clientName;
	private String clientContactNo;
	private String dropboxAuthKey;
	private String userRootDirectory;
	private String userAccessKey;
	private boolean serverService = false;
	private boolean serverLoadOnStartup = false;
	private boolean serverAutoSync = false;
	private String emailId;

	public UserVO() {
		
	}
		 
	public static synchronized UserVO getInstance() {
		if(CommanUtil.getUserSettingsVO()==null) {
			CommanUtil.setUserSettingsVO(new UserVO());
		}
		return CommanUtil.getUserSettingsVO();
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientContactNo() {
		return clientContactNo;
	}

	public void setClientContactNo(String clientContactNo) {
		this.clientContactNo = clientContactNo;
	}

	public String getDropboxAuthKey() {
		return dropboxAuthKey;
	}

	public void setDropboxAuthKey(String dropboxAuthKey) {
		this.dropboxAuthKey = dropboxAuthKey;
	}

	public String getUserRootDirectory() {
		return userRootDirectory;
	}

	public void setUserRootDirectory(String userRootDirectory) {
		this.userRootDirectory = userRootDirectory;
	}

	public String getUserAccessKey() {
		return userAccessKey;
	}

	public void setUserAccessKey(String userAccessKey) {
		this.userAccessKey = userAccessKey;
	}

	public boolean isServerService() {
		return serverService;
	}

	public void setServerService(boolean serverService) {
		this.serverService = serverService;
	}

	public boolean isServerLoadOnStartup() {
		return serverLoadOnStartup;
	}

	public void setServerLoadOnStartup(boolean serverLoadOnStartup) {
		this.serverLoadOnStartup = serverLoadOnStartup;
	}

	public boolean isServerAutoSync() {
		return serverAutoSync;
	}

	public void setServerAutoSync(boolean serverAutoSync) {
		this.serverAutoSync = serverAutoSync;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
		
	
}
