package com.user.info;
/**
 * 
 * @author sdahake
 *
 */
public class UserVO {
	
	private String userName;
	private String password;
	private String clientName;
	private String clientContactNo;
	private String dropboxAuthKey;

	public UserVO() {
		 
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

	
}
