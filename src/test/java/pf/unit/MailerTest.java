package pf.unit;//package pf.unit;
//
//import org.junit.Test;
//import pf.email.Mailer;
//import pf.email.SendMail;
//
//import javax.mail.MessagingException;
//
//public class MailerTest {
//
//
//	@Test
//	public void testSendVerifyEmail() throws Exception {
//		String rootPath = TestConfig.getTestRootPath();
//		pf.Settings.setup(rootPath);
//		new Mailer().sendVerifyEmail("ahm507@gmail.com", "12345");
//		//Just not throwing any exception means it is OK.
//	}
//
//	@Test
//	public void testSendResetEmail() throws Exception {
//		String rootPath = TestConfig.getTestRootPath();
//		pf.Settings.setup(rootPath);
//		new Mailer().sendResetEmail("ahm507@gmail.com", "reset-code");
//		//Just not throwing any exception means it is OK.
//	}
//
//	@Test
//	public void testSendFeedbackEmail() throws Exception {
//		String rootPath = TestConfig.getTestRootPath();
//		pf.Settings.setup(rootPath);
//		Mailer mailer = new Mailer();
//		mailer.sendFeedbackEmail("ahm507@gmail.com", "Mr Ahmed", "title", "comments");
//		//Just not throwing any exception means it is OK.
//	}
//
//
//	public static class SendMailTest {
//
//        //This production code tested is covered somewhere else except the CC and BCC
//
//        @Test
//        public void testSendAuthZohoMail() throws MessagingException {
//
//            SendMail sendMail = new SendMail();
//
//            String recipientsTo[] = new String[] {"ahm507@gmail.com", "ahammad@itida.gov.eg"};
//            String recipientsCC[] = new String[] {"ahm507@gmail.com", "ahammad@itida.gov.eg"};
//            String recipientsBCC[] = new String[] {"ahm507@gmail.com", "ahammad@itida.gov.eg"};
//
//            sendMail.sendAuthZohoMail("smtp.zoho.com",
//                    "support@salarycontrol.com",
//                    "allfree2allfree",
//                    recipientsTo,
//                    recipientsCC,
//                    recipientsBCC,
//                    "mail subject",
//                    "message message",
//                    "support@salarycontrol.com", //from
//                    false);
//            //Passing without exceptions is good enough
//
//        }
//
//    }
//}
