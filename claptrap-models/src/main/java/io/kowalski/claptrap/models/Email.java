package io.kowalski.claptrap.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.mail.Address;

public class Email implements Comparable<Email>, Message {

    private static final long serialVersionUID = -2056658768398234357L;

    private final UUID id;

    private Server server;
    private String sender;
    private List<Address> recipients;
    private String subject;
    private String htmlBody;
    private String plainBody;
    private String receivedOn;
    private boolean htmlEmail;

    public Email() {
        id = UUID.randomUUID();
    }

    public final Server getServer() {
        return server;
    }

    public final void setServer(final Server server) {
        this.server = server;
    }

    public final String getSender() {
        return sender;
    }

    public final void setSender(final String sender) {
        this.sender = sender;
    }

    public final List<Address> getRecipients() {
        return recipients;
    }

    public final void setRecipients(final List<Address> recipients) {
        this.recipients = recipients;
    }

    public final String getSubject() {
        return subject;
    }

    public final void setSubject(final String subject) {
        this.subject = subject;
    }

    public final String getHtmlBody() {
        return htmlBody;
    }

    public final void setHtmlBody(final String htmlBody) {
        this.htmlBody = htmlBody;
    }

    public final String getPlainBody() {
        return plainBody;
    }

    public final void setPlainBody(final String plainBody) {
        this.plainBody = plainBody;
    }

    public final String getReceivedOn() {
        return receivedOn;
    }

    public final void setReceivedOn(final String receivedOn) {
        this.receivedOn = receivedOn;
    }

    public final UUID getId() {
        return id;
    }

    public final boolean isHtmlEmail() {
        return htmlEmail;
    }

    public final void setHtmlEmail(final boolean htmlEmail) {
        this.htmlEmail = htmlEmail;
    }

    @Override
    public int compareTo(final Email o) {
        final SimpleDateFormat sdf = new SimpleDateFormat();
        try {
            Date thisDate = sdf.parse(this.getReceivedOn());
            Date oDate = sdf.parse(o.getReceivedOn());
            return thisDate.compareTo(oDate);
        } catch (ParseException e) {
        }

        return o.receivedOn.compareTo(receivedOn);
    }
}
