package pf.email;

import org.springframework.beans.factory.annotation.Value;

public class Mailer {

//	private Settings settings;
	@Value("${smtp.host}")
	private String smtp;
	@Value("${smtp.user}")
	private String user;
	@Value("${smtp.password}")
	private String password;
	
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

	
	public boolean sendVerifyEmail(String email, String uuid) throws Exception {
//		boolean isSendMail = Settings.getObject().isSendMail();
		SendMail mail = new SendMail();
//		loadSettings();
		String mailBody = String.format(registerMailBody, email, uuid);
		mail.sendZohoMail( smtp, user, password, email, registerMailSubject, mailBody, registerSendFrom);
		//FIXME: why retuen value!!
		return true;
	}

	public void sendResetEmail(String email, String resetCode) throws Exception {
//		boolean  reallySendMail =  Settings.getObject().isSendMail();
		//Send email
		pf.email.SendMail mail = new pf.email.SendMail();
//		Settings settings = Settings.getObject();
//		loadSettings();
		String mailBody = String.format(resetMailBody, email, resetCode);
		mail.sendZohoMail( smtp, user, password, email, resetMailSubject, mailBody,
				resetMailFrom);
	}


	public void sendFeedbackEmail(String email, String name, String title, String comments) throws Exception {
		//Send email
		assert name != null;
		assert title != null;
//		boolean  reallySendMail =  Settings.getObject().isSendMail();
		pf.email.SendMail mail = new pf.email.SendMail();
//		loadSettings();
		String sendFrom = email;
		String mailSubject = "Support: " + title;
		String mailBody = comments;
		mail.sendZohoMail( smtp, user, password, email, mailSubject, mailBody, sendFrom);
	}


}
