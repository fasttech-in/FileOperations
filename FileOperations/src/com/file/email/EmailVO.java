package com.file.email;

import java.util.List;

public class EmailVO {
	
	private List<String> toEmailIds;
	private List<String> attachmentPaths;
	private String message;

	public EmailVO(List<String> emailids,
			List<String> attachmentPaths,
			String message) {
		this.toEmailIds = emailids;
		this.attachmentPaths = attachmentPaths;
		this.message = message;
	}
	
	public void sendEmail(EmailVO emailVO) {
		// Save object to DB and trigger scheduler
	}

}
