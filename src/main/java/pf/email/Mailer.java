package pf.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Mailer {

//	private Settings settings;
	@Value("${smtp.host}")
	private String smtpHost;
	
	@Value("${smtp.user}")
	private String smtpUser;
	
	@Value("${smtp.password}")
	private String smtpPassword;
	
	@Value("${registerVerifyEmail.from}")
	String registerSendFrom;// = settings.getRegisterMailFrom();
	@Value("${registerVerifyEmail.subject}")
	String registerMailSubject;// = settings.getRegisterMailSubject();
	@Value("${registerVerifyEmail.body}")
	String registerMailBody;// = settings.getRegisterMailSubject();
	
	@Value("${passwordReset.from}")
	String resetMailFrom;
	@Value("${passwordReset.subject}")
	String resetMailSubject;
	@Value("${passwordReset.body}")
	String resetMailBody;

	
	public void sendVerifyEmail(String email, String uuid) throws Exception {
		Zoho mail = new Zoho();
		String mailBody = String.format(registerMailBody, email, uuid);
		mail.sendZohoMail( smtpHost, smtpUser, smtpPassword, email, registerMailSubject, mailBody, registerSendFrom);
	}

	public void sendResetEmail(String email, String resetCode) throws Exception {
		pf.email.Zoho mail = new pf.email.Zoho();
		String mailBody = String.format(resetMailBody, email, resetCode);
		mail.sendZohoMail( smtpHost, smtpUser, smtpPassword, email, resetMailSubject, mailBody,
				resetMailFrom);
	}



	public void sendFeedbackEmail(String email, String name, String title, String comments) throws Exception {
		//Send email
		assert name != null;
		assert title != null;
		pf.email.Zoho mail = new pf.email.Zoho();
		String sendFrom = email;
		String mailSubject = "Support: " + title;
		String mailBody = comments;
		mail.sendZohoMail( smtpHost, smtpUser, smtpPassword, email, mailSubject, mailBody, sendFrom);
	}

	public void sendWeeklyReport(String to, String subject, String body) throws Exception {
		//Send email
		assert to != null;
		assert subject != null;
		Zoho mail = new Zoho();
		String sendFrom = "support@salarycontrol.com";
		mail.sendZohoMail( smtpHost, smtpUser, smtpPassword, to, subject, body, sendFrom);
	}
	
	
	public void sendBackupEmail(String to, String subject, String body, String fileFullPath) throws Exception {
		//Send email
		assert to != null;
		assert subject != null;
		Zoho mail = new Zoho();
		String sendFrom = "support@salarycontrol.com";
		mail.sendMailMultiPart( smtpHost, smtpUser, smtpPassword, to, subject, body, sendFrom, fileFullPath);
	}

	
}
