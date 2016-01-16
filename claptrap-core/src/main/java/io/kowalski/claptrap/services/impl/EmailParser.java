package io.kowalski.claptrap.services.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.mail.Address;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.util.MimeMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.models.Server;
import io.kowalski.claptrap.services.Parser;

public class EmailParser implements Parser<MimeMessage, Email> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailParser.class);

    private final BlockingQueue<MimeMessage> mimeMessageQueue;
    private final EmailStorageService emailStorageService;

    @Inject
    public EmailParser(final BlockingQueue<MimeMessage> mimeMessageQueue, final EmailStorageService emailStorageService) {
        this.mimeMessageQueue = mimeMessageQueue;
        this.emailStorageService = emailStorageService;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                final MimeMessage mimeMessage = mimeMessageQueue.take();
                final Email email = parseMessage(mimeMessage);
                emailStorageService.store(email);
            } catch (final Exception e) {
                LOGGER.error("Unable to parse message... Adding to \"Exception\" Server.", e);
                final Email email = createExceptionEmail(e);
                emailStorageService.store(email);
                continue; // Keep on processing...
            }
        }
    }

    @Override
    public Email parseMessage(final MimeMessage mimeMessage) throws Exception {

        final MimeMessageParser messageParser = new MimeMessageParser(mimeMessage).parse();
        final Email email = new Email();

        email.setServer(new Server(retrieveServerName(messageParser.getFrom())));
        email.setSender(messageParser.getFrom());

        final List<Address> recipients = new ArrayList<Address>();
        recipients.addAll(messageParser.getTo());
        recipients.addAll(messageParser.getCc());
        recipients.addAll(messageParser.getBcc());

        email.setRecipients(recipients);
        email.setSubject(messageParser.getSubject());

        if (messageParser.getHtmlContent() != null && !messageParser.getHtmlContent().isEmpty()) {
            email.setHtmlBody(messageParser.getHtmlContent());
            email.setPlainBody(messageParser.getPlainContent());
            email.setHtmlEmail(true);
        } else {
            email.setPlainBody(messageParser.getPlainContent());
            email.setHtmlEmail(false);
        }

        email.setReceivedOn(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        return email;
    }


    private Email createExceptionEmail(final Exception e) {
        final Email email = new Email();

        email.setServer(new Server("java.lang.exception"));
        email.setSender("claptrap@claptrap-internal.jar");
        email.setSubject("Exception occurred processing email");
        email.setPlainBody(e.getMessage());
        email.setReceivedOn(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        return email;

    }

    private String retrieveServerName(final String from) {
        return from.substring(from.lastIndexOf('@') + 1).toLowerCase();
    }

}
