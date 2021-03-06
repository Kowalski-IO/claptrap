package io.kowalski.claptrap.api.embedded;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;

import io.kowalski.claptrap.api.external.EmbeddedMailbox;
import io.kowalski.claptrap.exception.ClaptrapException;
import io.kowalski.claptrap.models.Email;
import jersey.repackaged.com.google.common.collect.Lists;

public class EmbeddedMailboxTest {

    private static final String hostname = "localhost";
    private static EmbeddedMailbox mailbox;
    private static int port;

    @BeforeClass
    public static void beforeClass() throws ClaptrapException {
        mailbox = new EmbeddedMailbox();
        port = mailbox.getSMTPPort();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        mailbox.close();
    }

    @Test
    public void allEmails() throws EmailException, ClaptrapException {
        seedAllEmails();
        final Collection<Email> fetchedEmails = mailbox.fetchAll();

        assertEquals("sendSimpleEmail sends 3 emails total", 3, fetchedEmails.size());

        final Set<String> emailAddresses = new HashSet<String>();
        for (final Email email : fetchedEmails) {
            emailAddresses.add(email.getRecipient());
        }

        assertTrue(emailAddresses.contains("john.johnson@gmail.com"));
        assertTrue(emailAddresses.contains("frank@underwood.us"));
        assertTrue(emailAddresses.contains("brandon@kowalski.io"));

    }

    @Test
    public void allEmailsForEnvironment() throws ClaptrapException, EmailException {
        seedForEnvironmentEmails();

        final Collection<Email> fetchedEmails = mailbox.fetchAllForEnvironment("test.claptrap.kowalski.io");

        assertEquals("seedForEnvironmentEmails sends 2 emails total for server forEnvironment", 2, fetchedEmails.size());

    }

    @Test
    public void allEmailsForCriteria() throws EmailException, ClaptrapException {
        seedForCriteriaEmail();

        final Predicate<?,?> recipient = Predicates.equal("recipient", "nugget@kowalski.io");
        final Predicate<?,?> sender = Predicates.equal("sender", "criteria@claptrap.kowalski.io");
        final Predicate<?,?> filter = Predicates.and(recipient, sender);

        final List<Email> fetchedEmails = Lists.newArrayList(mailbox.fetchForCriteria(filter));

        assertEquals("seedForEnvironmentEmails sends 1 emails total to Nugget", 1, fetchedEmails.size());

        assertEquals("nugget@kowalski.io", fetchedEmails.get(0).getRecipient());
    }

    private void seedAllEmails() throws EmailException {
        final SimpleEmail email = new SimpleEmail();
        email.setHostName(hostname);
        email.setSmtpPort(port);

        email.addTo("john.johnson@gmail.com", "John Johnson");
        email.addTo("frank@underwood.us", "Frank Underwood");
        email.addTo("brandon@kowalski.io", "Brandon Kowalski");
        email.setFrom("allEmails@claptrap.kowalski.io", "Claptrap");
        email.setSubject("Test simple email");

        email.setMsg("This is a simple email test to see if Claptrap actually works!");

        email.send();

    }

    private void seedForEnvironmentEmails() throws EmailException {
        final SimpleEmail email = new SimpleEmail();
        email.setHostName(hostname);
        email.setSmtpPort(port);

        email.addTo("john.johnson@gmail.com", "John Johnson");
        email.addTo("frank@underwood.us", "Frank Underwood");
        email.setFrom("forEnvironment@test.claptrap.kowalski.io", "Claptrap");
        email.setSubject("Test simple email");

        email.setMsg("This is a simple email test to see if Claptrap actually works!");

        email.send();

        final SimpleEmail email2 = new SimpleEmail();
        email2.setHostName(hostname);
        email2.setSmtpPort(port);

        email2.addTo("john.johnson@gmail.com", "John Johnson");
        email2.addTo("frank@underwood.us", "Frank Underwood");
        email2.addTo("brandon@kowalski.io", "Brandon Kowalski");
        email2.setFrom("ingoredEmails@claptrap.kowalski.io", "Claptrap");
        email2.setSubject("Test simple email");

        email2.setMsg("This is a simple email test to see if Claptrap actually works!");

        email2.send();

    }

    private void seedForCriteriaEmail() throws EmailException {
        final SimpleEmail email = new SimpleEmail();
        email.setHostName(hostname);
        email.setSmtpPort(port);

        email.addTo("john.johnson@gmail.com", "John Johnson");
        email.addTo("frank@underwood.us", "Frank Underwood");
        email.addTo("brandon@kowalski.io", "Brandon Kowalski");
        email.addTo("nugget@kowalski.io", "Nugget the Ragdoll");
        email.setFrom("criteria@claptrap.kowalski.io", "Claptrap");
        email.setSubject("Test simple email");

        email.setMsg("This is a simple email test to see if Claptrap actually works!");

        email.send();

    }

}
