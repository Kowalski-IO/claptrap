import com.sun.mail.smtp.SMTPTransport;
import lombok.SneakyThrows;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

public class EmailTest {

    /**
     * Hack to show BCC in Claptrap during testing.
     */
    @BeforeClass
    public static void hackTransport() {
        try {
            Field ignoreList = SMTPTransport.class.getDeclaredField("ignoreList");
            ignoreList.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(ignoreList, ignoreList.getModifiers() & ~Modifier.FINAL);

            ignoreList.set(null, new String[]{});

        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void sendPlainEmail() throws EmailException, MessagingException {
        final SimpleEmail email = new SimpleEmail();
        email.setHostName("localhost");
        email.setSmtpPort(2525);

        email.addTo("john.johnson@gmail.com", "John Johnson");
        email.addTo("frank@underwood.us", "Frank Underwood");
        email.addTo("brandon@kowalski.io", "Brandon Kowalski");

        email.addCc("lisa@timparsons.io", "Lisa Parsons");

        email.addBcc("brandon.cruz@cornell.edu", "Brandon Cruz");

        email.setFrom("potato@salad.com", "Claptrap");
        email.setSubject("Test simple email");

        email.setMsg("This is a simple email test to see if Claptrap actually works!");

        email.send();

        assert email.getMimeMessage().getMessageID() != null;
    }

    @Test
    public void sendPlainEmail2() throws EmailException, MessagingException {
        final SimpleEmail email = new SimpleEmail();
        email.setHostName("localhost");
        email.setSmtpPort(2525);

        email.addTo("john.jakob@jinglehimmer.com", "John John");
        email.addTo("tim@timparsons.io", "Tim Parsons");

        email.addBcc("janet.heslop@cornell.edu", "Janet Heslop");

        email.setFrom("demo@test.com", "Claptrap");
        email.setReplyTo(new ArrayList<>(Arrays.asList(new InternetAddress("replyto@test.com"))));
        email.setSubject("Test simple email");

        email.setMsg("This is a simple email test to see if Claptrap actually works!");

        email.send();

        assert email.getMimeMessage().getMessageID() != null;
    }

    @Test
    public void sendPlainEmail3() throws EmailException, MessagingException {
        final SimpleEmail email = new SimpleEmail();
        email.setHostName("localhost");
        email.setSmtpPort(2525);

        email.addTo("matthew@kowalski.io", "Matthew Kowalski");

        email.setFrom("tim@parsons.io", "Claptrap");
        email.setSubject("Test simple email");

        email.setMsg("This is a simple email test to see if Claptrap actually works!");

        email.send();

        assert email.getMimeMessage().getMessageID() != null;
    }

    @Test
    public void sendPlainEmail4() throws EmailException, MessagingException {
        for (int i = 1; i <= 100; i++) {

            String from = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

            final SimpleEmail email = new SimpleEmail();
            email.setHostName("localhost");
            email.setSmtpPort(2525);

            email.addTo("fancy@kowalski.io", "Fancy Kowalski");

            email.setFrom(from.concat("@fancy.io"), from);
            email.setSubject("Test simple email ".concat(String.valueOf(i)));

            email.setMsg("This is a simple email test to see if Claptrap actually works!");

            email.send();

            assert email.getMimeMessage().getMessageID() != null;
        }
    }

    @Ignore
    @Test
    public void sendPlainEmail5() throws EmailException, MessagingException {
        for (int i = 97; i <= 107; i++) {

            String from = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

            final SimpleEmail email = new SimpleEmail();
            email.setHostName("localhost");
            email.setSmtpPort(2525);

            email.addTo("fancy@kowalski.io", "Fancy Kowalski");

            char c = (char) i;

            email.setFrom(from.concat("@").concat(String.valueOf(c)).concat("fancy.io"), from);
            email.setSubject("Test simple email ".concat(String.valueOf(i)));

            email.setMsg("This is a simple email test to see if Claptrap actually works!");

            email.send();

            assert email.getMimeMessage().getMessageID() != null;
        }
    }

    @Test
    public void sendHTMLEmail() throws EmailException {

        final HtmlEmail email = new HtmlEmail();

        email.setHostName("localhost");
        email.setSmtpPort(2525);

        email.addTo("john.johnson@gmail.com", "John Johnson");
        email.addTo("frank@underwood.us", "Frank Underwood");
        email.addTo("brandon@kowalski.io", "Brandon Kowalski");
        email.addTo("matthew@kowalski.io", "Matthew Kowalski");

        email.addCc("tim@timparsons.io", "Tim Parsons");

        email.addBcc("brandon.cruz@cornell.edu", "Brandon Cruz");

        email.setFrom("sl@jackson.com", "Samuel L. Jackson");
        email.setSubject("Test simple email");

        email.setMsg("Normally, both your asses would be dead as fucking fried chicken, but you happen to pull this shit while I'm in a transitional period so I don't wanna kill you, I wanna help you.");
        email.setHtmlMsg("Normally, both your asses would be dead as fucking fried chicken, but you happen to pull this shit while I'm in a transitional period so I don't wanna kill you, I wanna help you.<br><img src=\"https://media.giphy.com/media/VWjb4jBPSwOLm/giphy.gif\">");

        email.send();

    }

    @Test
    @SneakyThrows
    public void sendHTMLEmailWithAttachment() {
        final HtmlEmail email = new HtmlEmail();

        email.setHostName("localhost");
        email.setSmtpPort(2525);

        email.addTo("william.fancyson@gmail.com");

        email.setFrom("beta@kowalski.io", "Claptrap");
        email.setSubject("Test simple email");

        email.setMsg("This is an email from beta@kowalski.io!");
        email.setHtmlMsg("This is a simple email test to see if Claptrap actually works!<br><a href=\"https://kowalski.io\">Kowalski.io</a>");

        final EmailAttachment attachment = new EmailAttachment();
        attachment.setName("patrick.gif");
        attachment.setDescription("I'm Squidward!");
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setURL(new URL("https://i.imgur.com/h40NmCM.gif"));

        email.attach(attachment);

        email.send();
    }


}
