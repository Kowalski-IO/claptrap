package io.kowalski.claptrap.test;

import static org.junit.Assert.assertTrue;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.junit.Test;

import io.kowalski.Claptrap;

public class EmailTest {

    private final String host = "localhost";
    private final int smtpPort;

    private final Claptrap claptrap;

    public EmailTest() throws Exception {
        claptrap = new Claptrap();
        claptrap.run("server", "test_configuration.yml");
        smtpPort = claptrap.getSmtpPort();
    }

    @Test
    public void sendSimpleEmail() throws EmailException {
        final SimpleEmail email = new SimpleEmail();
        email.setHostName(host);
        email.setSmtpPort(smtpPort);

        email.addTo("john.johnson@kgmail.com", "John Johnson");
        email.addTo("frank@underwood.us", "Frank Underwood");
        email.addTo("brandon@kowalski.io", "Brandon Kowalski");
        email.setFrom("me@claptrap.kowalski.io", "Claptrap");
        email.setSubject("Test simple email");

        email.setMsg("This is a simple email test to see if Claptrap actually works!");


        final String messageID = email.send();

        assertTrue(messageID != null);
        assertTrue(!"".equals(messageID));
        assertTrue(messageID.length() > 0);
    }

}
