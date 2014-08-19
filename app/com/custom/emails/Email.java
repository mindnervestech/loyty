package com.custom.emails;


import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import play.Play;
import utils.EmailExceptionHandler;

public class Email extends Authenticator {
	
	public static void sendOnlyMail(String recipient,String subject, String body){
		
		final String username;
		final String password;
		
		Properties props = new Properties();
		
			username = Play.application().configuration().getString("smtp.user");
			password = Play.application().configuration().getString("smtp.password");
			props = new Properties();
			
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.debug", "true");
			
			props.put("mail.smtp.host", Play.application().configuration().getString("smtp.host"));
			props.put("mail.smtp.port", Play.application().configuration().getString("smtp.port"));
		
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		try {	
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipient));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
 
		} catch (MessagingException e) {
			EmailExceptionHandler.handleException(e);
		}
	}
}
