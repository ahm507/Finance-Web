/*
This is a free source code and is provided as it is without any warranties and
it can be used in any your code for free.

Author : Sudhir Ancha
 */
package pf.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMail {

	public void sendZohoMail(String smtpAddress, String userName, String password,
			String recipient, String subject, String message, String from)
			throws MessagingException {
		String recipientsTo[] = new String[1];
		recipientsTo[0] = recipient;
		sendAuthZohoMail(smtpAddress, userName, password, recipientsTo, null, null,
				subject, message, from);
	}

	public void sendAuthZohoMail(String smtpAddress, String userName, String password,
			String recipientsTo[], String recipientsCC[],
			String recipientsBCC[], String subject, String message, String from)
			throws MessagingException {
		// Set the host smtp address
		Properties props = new Properties();
	    props.setProperty("mail.transport.protocol", "smtp");     
		props.put("mail.smtp.host", smtpAddress);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "false"); //false for zohomail
		
		props.put("mail.smtp.EnableSSL.enable", "true");
//		props.put("mail.debug", "true");

		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", "465");

		Authenticator auth = new SMTPAuthenticator(userName, password);
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(false);
		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		// Set TO
		if (recipientsTo != null) {
			InternetAddress[] addressTo = new InternetAddress[recipientsTo.length];
			for (int i = 0; i < recipientsTo.length; i++)
				addressTo[i] = new InternetAddress(recipientsTo[i]);
			msg.setRecipients(Message.RecipientType.TO, addressTo);
		}
		// Set CC
		if (recipientsCC != null) {
			InternetAddress[] addressCC = new InternetAddress[recipientsCC.length];
			for (int i = 0; i < recipientsCC.length; i++)
				addressCC[i] = new InternetAddress(recipientsCC[i]);
			msg.setRecipients(Message.RecipientType.CC, addressCC);
		}

		// Set BCC
		if (recipientsBCC != null) {
			InternetAddress[] addressBCC = new InternetAddress[recipientsBCC.length];
			for (int i = 0; i < recipientsBCC.length; i++)
				addressBCC[i] = new InternetAddress(recipientsBCC[i]);
			msg.setRecipients(Message.RecipientType.BCC, addressBCC);
		}

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");

		//reallySendMail is used primary to stop sending too much emails in testing
			Transport.send(msg);

	}
	
	
	
// DEAD CODE
// GMAIL became very annoying because of strict security policies.
//
//	public void sendGMail(String smtpAddress, String userName, String password,
//			String recipient, String subject, String message, String from)
//			throws MessagingException {
//		String recipientsTo[] = new String[1];
//		recipientsTo[0] = recipient;
//		sendGMail(smtpAddress, userName, password, recipientsTo, null, null,
//				subject, message, from);
//	}
//
//	public void sendGMail(String smtpAddress, String userName, String password,
//			String recipientsTo[], String recipientsCC[],
//			String recipientsBCC[], String subject, String message, String from)
//			throws MessagingException {
//		// Set the host smtp address
//		Properties props = new Properties();
//		props.put("mail.smtp.host", smtpAddress);
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.EnableSSL.enable", "true");
//		props.put("mail.debug", "true");
//		props.put("mail.debug", "true");
//		props.put("mail.smtp.port", "25");
//
//		props.setProperty("mail.smtp.socketFactory.class",
//				"javax.net.ssl.SSLSocketFactory");
//		props.setProperty("mail.smtp.socketFactory.fallback", "false");
//		props.setProperty("mail.smtp.port", "465");
//		props.setProperty("mail.smtp.socketFactory.port", "465");
//
//		Authenticator auth = new SMTPAuthenticator(userName, password);
//		Session session = Session.getDefaultInstance(props, auth);
//		session.setDebug(false);
//		Message msg = new MimeMessage(session);
//		InternetAddress addressFrom = new InternetAddress(from);
//		msg.setFrom(addressFrom);
//
//		// Set TO
//		if (recipientsTo != null) {
//			InternetAddress[] addressTo = new InternetAddress[recipientsTo.length];
//			for (int i = 0; i < recipientsTo.length; i++)
//				addressTo[i] = new InternetAddress(recipientsTo[i]);
//			msg.setRecipients(Message.RecipientType.TO, addressTo);
//		}
//		// Set CC
//		if (recipientsCC != null) {
//			InternetAddress[] addressCC = new InternetAddress[recipientsCC.length];
//			for (int i = 0; i < recipientsCC.length; i++)
//				addressCC[i] = new InternetAddress(recipientsCC[i]);
//			msg.setRecipients(Message.RecipientType.CC, addressCC);
//		}
//
//		// Set BCC
//		if (recipientsBCC != null) {
//			InternetAddress[] addressBCC = new InternetAddress[recipientsBCC.length];
//			for (int i = 0; i < recipientsBCC.length; i++)
//				addressBCC[i] = new InternetAddress(recipientsBCC[i]);
//			msg.setRecipients(Message.RecipientType.BCC, addressBCC);
//		}
//
//		// Setting the Subject and Content Type
//		msg.setSubject(subject);
//		msg.setContent(message, "text/plain");
//		Transport.send(msg);
//
//	}

	/**
	 * SimpleAuthenticator is used to do simple authentication when the SMTP
	 * server requires it.
	 */
	private class SMTPAuthenticator extends Authenticator {
		String userName, password;

		SMTPAuthenticator(String userName, String password) {
			this.userName = userName;
			this.password = password;
		}

		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(userName, password);
		}
	}

}
