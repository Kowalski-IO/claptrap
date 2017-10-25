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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;

//@Ignore
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
    @Ignore
    public void sendPlainEmail() throws EmailException, MessagingException {
        final SimpleEmail email = new SimpleEmail();
        email.setHostName("localhost");
        email.setSmtpPort(2525);

        email.addTo("john.johnson@gmail.com", "John Johnson");
        email.addTo("frank@underwood.us", "Frank Underwood");
        email.addTo("brandon@kowalski.io", "Brandon Kowalski");

        email.addCc("tim@timparsons.io", "Tim Parsons");

        email.addBcc("brandon.cruz@cornell.edu", "Brandon Cruz");

        email.setFrom("allEmails@claptrap.kowalski.io", "Claptrap");
        email.setSubject("Test simple email");

        email.setMsg("This is a simple email test to see if Claptrap actually works!");

        email.send();

        assert email.getMimeMessage().getMessageID() != null;
    }

    @Test
    public void sendHTMLEmail() throws EmailException {

        for (int i = 0; i < 8000; i++) {

            final HtmlEmail email = new HtmlEmail();

            email.setHostName("localhost");
            email.setSmtpPort(2525);

            email.addTo("john.johnson@gmail.com", "John Johnson");
            email.addTo("frank@underwood.us", "Frank Underwood");
            email.addTo("brandon@kowalski.io", "Brandon Kowalski");

            email.addCc("tim@timparsons.io", "Tim Parsons");

            email.addBcc("brandon.cruz@cornell.edu", "Brandon Cruz");

            email.setFrom("allEmails@claptrap.kowalski.io", "Claptrap");
            email.setSubject("Test simple email");

            email.setMsg("This is a simple email test to see if Claptrap actually works!");
            email.setHtmlMsg("This is a simple email test to see if Claptrap actually works!<br><img src=\"https://media.giphy.com/media/VWjb4jBPSwOLm/giphy.gif\">");

            email.send();
        }

    }

    @Test
    @SneakyThrows
    @Ignore
    public void sendHTMLEmailWithAttachment() {
        final HtmlEmail email = new HtmlEmail();

        email.setHostName("localhost");
        email.setSmtpPort(2525);

        email.addTo("john.johnson@gmail.com", "John Johnson");
        email.addTo("frank@underwood.us", "Frank Underwood");
        email.addTo("brandon@kowalski.io", "Brandon Kowalski");

        email.addCc("tim@timparsons.io", "Tim Parsons");

        email.addBcc("brandon.cruz@cornell.edu", "Brandon Cruz");

        email.setFrom("allEmails@claptrap.kowalski.io", "Claptrap");
        email.setSubject("Test simple email");

        email.setMsg("This is a simple email test to see if Claptrap actually works!");
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
