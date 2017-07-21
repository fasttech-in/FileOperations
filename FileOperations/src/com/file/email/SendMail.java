package com.file.email;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.file.ui.FileTreeView;
import com.user.info.UserVO;

public class SendMail {
	static Logger log = Logger.getLogger(SendMail.class.getName());
	
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
			BodyPart messageBodyPart = new MimeBodyPart();
			String msg = getMessage(emailVO.getAttachmentPaths());
	        messageBodyPart.setText(msg);
	        messageBodyPart.setContent(msg, "text/html");
	        multipart.addBodyPart(messageBodyPart);
			
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
			log.info(e);
			e.printStackTrace();
		}
		return result;
	}

	private static String getMessage(List<String> attachmentPaths) {
		String msg = "Hello,<br> Thank you for your business. <br><br>Following files are attached with mail - <br>";
		for (int i = 0; i < attachmentPaths.size(); i++) {
			String filename = attachmentPaths.get(i);
			msg +=String.valueOf((i+1))+" "+new File(filename).getName()+"<br>";
		}
		msg +="<br><br>Thanks & Regards<br>"+UserVO.getInstance().getClientName()+"";
		msg +="<br><br>Mail sent from <br><b>Precise - Documents Management System.<br>fasttech.in@gmail.com | 9503462575/9421517509</b>";
		return msg;
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
			log.info(e);
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