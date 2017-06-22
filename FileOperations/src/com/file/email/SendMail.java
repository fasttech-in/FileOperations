package com.file.email;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {
	static String username = "noreplyfasttech@gmail.com";
	static String password = "fasttech2013";
	public final static String SUCCESS_MESSAGE = "Mail Send Successfully";

	public static String sendMailWithAttachment(EmailVO emailVO) throws Exception {

		String result="";
		Properties props = new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		username = emailVO.getUsername().isEmpty()?username:emailVO.getUsername();
		password = emailVO.getPassword().isEmpty()?password:emailVO.getPassword();

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {
			InternetAddress[] recipientsList = addRecipients(emailVO.getToEmailIds());
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,recipientsList);
			message.setSubject(emailVO.getSubject());
			message.setText(emailVO.getMessage());

			Multipart multipart = new MimeMultipart();
			addAttachment(multipart, emailVO.getAttachmentPaths());
			
			message.setContent(multipart);

			System.out.println("Sending");

			Transport.send(message);
			result = SUCCESS_MESSAGE;
			System.out.println("Mail sent.");

		} catch (Exception e) {
			if(e.getMessage().contains("Your message exceeded Google's message size limits")){
				result = "Max Allowed Attachment size 24 MB";
			}else{
				result = "Mail Not Send ";
			}
		
			e.printStackTrace();
		}
		return result;
	}

	private static void addAttachment(Multipart multipart,
			List<String> attachmentPaths) throws Exception {
		try {
			for (String filePath : attachmentPaths) {
				File file = new File(filePath);
				DataSource source = new FileDataSource(filePath);
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(file.getName());
				multipart.addBodyPart(messageBodyPart);
			}
		} catch (Exception e) {
			System.out.println("Exception In addAttachment() :"+e.getMessage());
			e.printStackTrace();		
			throw e;
		}

	}

	private static InternetAddress[] addRecipients(List<String> recipientAddressList) throws Exception {
		
		InternetAddress[] recipientAddress = new InternetAddress[recipientAddressList.size()];

		try {
			int counter = 0;
			for (String recipient : recipientAddressList) {
				recipientAddress[counter] = new InternetAddress(recipient.trim());
				counter++;
			}

		} catch (Exception e) {
			System.out.println("Exception In addRecipients() :"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return recipientAddress;

	}

}