/*
This is a free source code and is provided as it is without any warranties and
it can be used in any your code for free.

Author : Sudhir Ancha
 */
package pf.email;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;

public class Zoho {

	
	boolean sendEmail;
	public Zoho(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}
	
	private static final Logger log = LoggerFactory.getLogger(Zoho.class);

	
	public boolean sendZohoMail(String smtpAddress, String userName, String password, String recipient, String subject,
			String message, String from) throws MessagingException {
		String recipientsTo[] = new String[1];
		recipientsTo[0] = recipient;

		sendAuthZohoMail(smtpAddress, userName, password, recipientsTo, null, null, subject, message, from);

		return true;
	}

	public boolean sendMailMultiPart(String smtpAddress, String userName, String password, String recipient,
			String subject, String message, String from, String attachmentsFilePath) throws MessagingException {
		String recipientsTo[] = new String[1];
		recipientsTo[0] = recipient;

		sendMailWithAttachments(smtpAddress, userName, password, recipientsTo, null, null, subject, message, from,
				attachmentsFilePath);

		return true;
	}

	private void sendAuthZohoMail(String smtpAddress, String userName, String password, String recipientsTo[],
			String recipientsCC[], String recipientsBCC[], String subject, String message, String from)
			throws MessagingException {
		// Set the host smtp address
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", smtpAddress);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "false"); // false for zohomail

		props.put("mail.smtp.EnableSSL.enable", "true");
		// props.put("mail.debug", "true");

		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", "465");

		// Authenticator auth = new SMTPAuthenticator(userName, password);
		// Session session = Session.getDefaultInstance(props, auth);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});

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
		// msg.setContent(message, "text/plain");
		msg.setContent(message, "text/html");

		// reallySendMail is used primary to stop sending too much emails in
		// testing
		if (sendEmail) {
			Transport.send(msg);
		}

	}

	void sendMailWithAttachments(String smtpAddress, String userName, String password, String recipientsTo[],
			String recipientsCC[], String recipientsBCC[], String subject, String message, String from,
			String attachmentsFilePath) throws MessagingException {
		// Set the host smtp address
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", smtpAddress);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "false"); // false for zohomail

		props.put("mail.smtp.EnableSSL.enable", "true");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", "465");

		// Authenticator auth = new SMTPAuthenticator(userName, password);
		// Session session = Session.getDefaultInstance(props, auth);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});

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
		/////////////////////////////

		// Now set the actual message
		BodyPart messageBodyPart = new MimeBodyPart();
		// Part 1
//		messageBodyPart.setText(message);
		messageBodyPart.setContent(message, "text/html; charset=utf-8");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// Part two is attachment
		messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(attachmentsFilePath);
		messageBodyPart.setDataHandler(new DataHandler(source));
		//visibleFileName is visible in the email attachments
		String visibleFileName = attachmentsFilePath.substring(attachmentsFilePath.lastIndexOf('/')+1);
		messageBodyPart.setFileName(visibleFileName);
		multipart.addBodyPart(messageBodyPart);

		msg.setContent(multipart);

		// reallySendMail is used primary to stop sending too much emails in
		// testing
		if (sendEmail == true) {
			Transport.send(msg);
			log.info("Backup email is sent successfuly to " + recipientsTo[0]);			
		} else {
			log.info("Backup email is NOT sent to " + recipientsTo[0]);
 		}

	}

}
