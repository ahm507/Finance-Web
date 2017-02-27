package pf.bdd;

import pf.email.Mailer;

public class DummyMailer extends Mailer {

    public boolean sendVerifyEmail(String email, String uuid) throws Exception {
        return true;
    }


    public void sendResetEmail(String email, String resetCode) throws Exception {
    }


    public void sendFeedbackEmail(String email, String name, String title, String comments) throws Exception {
    }


}
