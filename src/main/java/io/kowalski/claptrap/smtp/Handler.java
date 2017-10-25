package io.kowalski.claptrap.smtp;

import io.kowalski.claptrap.models.*;
import io.kowalski.claptrap.services.ContactStorage;
import io.kowalski.claptrap.services.EmailStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.util.MimeMessageParser;
import org.subethamail.smtp.MessageHandler;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Handler implements MessageHandler {

    private final ContactStorage contactStorage;
    private final EmailStorage emailStorage;
    private final Map<String, Contact> recipients;

    private MimeMessage mimeMessage;
    private String from;

    Handler(ContactStorage contactStorage, EmailStorage emailStorage) {
        this.contactStorage = contactStorage;
        this.emailStorage = emailStorage;
        recipients = new HashMap<>();
    }

    @Override
    public void from(String from) {
        this.from = from;
    }

    @Override
    public void recipient(String recipient) {
        recipients.put(recipient, new Contact(recipient));
    }

    @Override
    public void data(InputStream data) {
        try {
            mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()), data);
        } catch (final MessagingException e) {
            log.error("Unable to parse MimeMessage", e);
        }
    }

    @Override
    public void done() {
        if (mimeMessage != null) {
            try {
                Email email = parseEmail();
                if (email != null) {
                    // contactStorage.store(email.getHeader().getAllRecipients());
                    emailStorage.store(email);
                }
            } catch (Exception e) {
                log.error("Unable to parse email.", e);
            }
        }
    }

    private Email parseEmail() throws Exception {
        final MimeMessageParser messageParser = new MimeMessageParser(mimeMessage).parse();

        final Header header = new Header();
        final Body body = new Body();

        final Email email = new Email(retrieveServerName(from));

        email.setHeader(header);
        email.setBody(body);

        header.setMessageID(mimeMessage.getMessageID());

        header.setDate(LocalDateTime.ofInstant(mimeMessage.getSentDate().toInstant(),
                ZoneId.systemDefault()));

        header.setFrom(parseFrom(mimeMessage, messageParser));

        header.setReplyTo(Arrays.asList(mimeMessage.getReplyTo())
                .parallelStream()
                .map(a -> Contact.of(a, ContactType.REPLY_TO))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElse(parseFrom(mimeMessage, messageParser)));

        header.setSender(Contact.of(mimeMessage.getSender(), ContactType.SENDER)
                .orElse(parseFrom(mimeMessage, messageParser)));

        header.setTo(messageParser.getTo()
                .parallelStream()
                .map(a -> Contact.of(a, ContactType.TO))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet()));

        header.setCc(messageParser.getCc()
                .parallelStream()
                .map(a -> Contact.of(a, ContactType.CC))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet()));

        header.setBcc(messageParser.getBcc()
                .parallelStream()
                .map(a -> Contact.of(a, ContactType.BCC))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet()));

        if (header.getBcc().isEmpty()) {
            header.getTo().forEach(c -> recipients.remove(c.getEmail()));
            header.getCc().forEach(c -> recipients.remove(c.getEmail()));
            header.setBcc(new HashSet<>(recipients.values()));
        }

        header.setSubject(messageParser.getSubject());
        header.setContentType(mimeMessage.getContentType());

        if (messageParser.getHtmlContent() != null && !messageParser.getHtmlContent().isEmpty()) {
            body.setHtml(messageParser.getHtmlContent());
        }

        body.setPlainText(messageParser.getPlainContent());

        if (messageParser.hasAttachments()) {
           email.getBody().setAttachments(messageParser.getAttachmentList()
                    .stream()
                    .map(ds -> Handler.parseAttachment(ds, email))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList()));
        }

        return email;
    }

    private Contact parseFrom(MimeMessage mimeMessage, MimeMessageParser messageParser) throws Exception {
        return Arrays.asList(mimeMessage.getFrom())
                .parallelStream()
                .map(a -> Contact.of(a, ContactType.FROM))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElse(new Contact(messageParser.getFrom()));
    }

    private static String retrieveServerName(String from) {
        return from.substring(from.lastIndexOf('@') + 1).toLowerCase();
    }

    private static Optional<Attachment> parseAttachment(DataSource ds, Email email) {
        try {
            Attachment attachment = new Attachment(ds.getName(), ds.getContentType());
            Path path = Paths.get("attachments/".concat(email.getId().toString()).concat("/").concat(attachment.getId().toString()));
            Files.createDirectories(path.getParent());
            Files.copy(ds.getInputStream(), path.toAbsolutePath());
            return Optional.of(attachment);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

}
