package io.kowalski.claptrap.handler;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.util.MimeMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.RejectException;
import org.subethamail.smtp.TooMuchDataException;

import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.storage.BroadcastService;
import io.kowalski.claptrap.storage.email.EmailStorageService;

public class SMTPMessageHandler implements MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMTPMessageHandler.class);

    private final EmailStorageService emailStorageService;
    private final BroadcastService<Email, String> emailBroadcastService;
    private final List<String> recipients;

    MimeMessage mimeMessage;
    private String from;

    public SMTPMessageHandler(final EmailStorageService emailStorageService,
            final BroadcastService<Email, String> emailBroadcastService) {
        this.emailStorageService = emailStorageService;
        this.emailBroadcastService = emailBroadcastService;
        recipients = new ArrayList<String>();
    }

    @Override
    public void from(final String from) throws RejectException {
        this.from = from;
    }

    @Override
    public void recipient(final String recipient) throws RejectException {
        recipients.add(recipient);
    }

    @Override
    public void data(final InputStream data) throws RejectException, TooMuchDataException, IOException {
        try {
            mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()), data);
        } catch (final MessagingException e) {
            LOGGER.error("Unable to parse MimeMessage", e);
        }
    }

    @Override
    public void done() {
        if (mimeMessage != null) {
            try {
                final List<Email> emails = parseMessages();
                emailStorageService.store(emails);
                emailBroadcastService.broadcast(emails);
            } catch (final Exception e) {
                LOGGER.error("Unable to create email", e);
                final Email exceptionEmail = createExceptionEmail(e);
                emailStorageService.store(exceptionEmail);
                emailBroadcastService.broadcast(exceptionEmail);
            }
        }
    }

    public List<Email> parseMessages() throws Exception {
        final List<Email> emails = new ArrayList<Email>();

        final MimeMessageParser messageParser = new MimeMessageParser(mimeMessage).parse();

        for (final String recipient : recipients) {
            final Email email = new Email();

            email.setServerName(retrieveServerName(from));
            email.setSender(from);
            email.setSubject(messageParser.getSubject());

            if (messageParser.getHtmlContent() != null && !messageParser.getHtmlContent().isEmpty()) {
                email.setHtmlBody(messageParser.getHtmlContent());
                email.setPlainBody(messageParser.getPlainContent());
                email.setHtmlEmail(true);
            } else {
                email.setPlainBody(messageParser.getPlainContent());
                email.setHtmlEmail(false);
            }

            email.setReceived(LocalDateTime.now());
            email.setRecipient(recipient.toString());

            emails.add(email);
        }

        return emails;
    }

    private Email createExceptionEmail(final Exception e) {
        final Email email = new Email();

        email.setServerName("Claptrap.error");
        email.setSender("claptrap@claptrap-internal.jar");
        email.setSubject("Exception occurred processing email");
        email.setPlainBody(e.getMessage());
        email.setReceived(LocalDateTime.now());

        return email;
    }

    private String retrieveServerName(final String from) {
        return from.substring(from.lastIndexOf('@') + 1).toLowerCase();
    }

}
