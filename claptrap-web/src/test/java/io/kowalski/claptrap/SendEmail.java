package io.kowalski.claptrap;

import java.net.MalformedURLException;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

public class SendEmail {

    private static final String HOSTNAME = "localhost";
    private static final int PORT = 2525;

    public static void main(final String[] args) throws EmailException, MalformedURLException, InterruptedException {
        sendHTMLEmail();
    }

    private static void sendHTMLEmail() throws EmailException, MalformedURLException {

        final HtmlEmail email = new HtmlEmail();
        email.setHostName(HOSTNAME);
        email.setSmtpPort(PORT);

        email.addCc("frank@underwood.us", "Frank Underwood");
        email.setFrom("claptrap@kowalski.io", "Claptrap");
        email.setSubject("Test HTML Email");

        // set the html message
        email.setHtmlMsg("<html><h1>The dumpster fire</h1><br><img src=\"http://www.lawyersgunsmoneyblog.com/wp-content/uploads/2015/11/dumpster-fire_medium.jpg\"></html>");

        // set the alternative message
        email.setTextMsg("No dumpster fire here cause you don't have HTML emails, sucka!");

        // send the email
        System.out.println(email.send());

    }

    private static void sendSimpleEmail() throws EmailException {
        final SimpleEmail email = new SimpleEmail();
        email.setHostName(HOSTNAME);
        email.setSmtpPort(PORT);

        email.addTo("john.minion@kowalski.io", "John Minion");
        email.addTo("sally.minion@kowalski.io", "Sally Minion");
        email.addTo("sally.minion@kowalski.io", "Sally Minion");
        email.setFrom("claptrap@claptrap.kowalski.io", "Claptrap");
        email.setSubject("Test simple email");

        // set the html message
        email.setMsg("Hi Mom!");

        // send the email
        System.out.println(email.send());
    }
}