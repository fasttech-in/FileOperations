package com.file.email;

import java.util.List;

import com.file.ui.PopupNotification;

public class EmailVO {
	
	private List<String> toEmailIds;
	private List<String> attachmentPaths;
	private String message;
	private String subject;
	private String username;
	private String password;

	public EmailVO(List<String> emailids,
			List<String> attachmentPaths,
			String message,String subject) {
		this.toEmailIds = emailids;
		this.attachmentPaths = attachmentPaths;
		this.message = message;
		this.subject = subject;
		initCredentials();
	}
	
	private void initCredentials() {
		username="";
		password="";
	}
	
	public List<String> getToEmailIds() {
		return toEmailIds;
	}

	public List<String> getAttachmentPaths() {
		return attachmentPaths;
	}

	public String getMessage() {
		return message;
	}

	public String getSubject() {
		return subject;
	}

	public void setToEmailIds(List<String> toEmailIds) {
		this.toEmailIds = toEmailIds;
	}

	public void setAttachmentPaths(List<String> attachmentPaths) {
		this.attachmentPaths = attachmentPaths;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String sendEmail(EmailVO emailVO) throws Exception {
		return SendMail.sendMailWithAttachment(emailVO); 
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}
