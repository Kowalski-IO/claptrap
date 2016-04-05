package io.kowalski.claptrap.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.kowalski.claptrap.models.annotations.Indexable;

public class Email implements Comparable<Email>, Serializable {

    private static final long serialVersionUID = -2056658768398234357L;

    @Indexable
    private final UUID id;

    @Indexable
    private String serverName;
    private String sender;

    @Indexable
    private String recipient;

    private String subject;
    private String htmlBody;
    private String plainBody;

    @Indexable
    @JsonProperty
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd' @ 'hh:mm:ss a")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime received;

    private boolean htmlEmail;

    public Email() {
        id = UUID.randomUUID();
    }

    public final UUID getId() {
        return id;
    }

    public final String getServerName() {
        return serverName;
    }

    public final void setServerName(final String serverName) {
        this.serverName = serverName;
    }

    public final String getSender() {
        return sender;
    }

    public final void setSender(final String sender) {
        this.sender = sender;
    }

    public final String getRecipient() {
        return recipient;
    }

    public final void setRecipient(final String recipient) {
        this.recipient = recipient;
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

    public final LocalDateTime getReceived() {
        return received;
    }

    public final void setReceived(final LocalDateTime received) {
        this.received = received;
    }

    public final boolean isHtmlEmail() {
        return htmlEmail;
    }

    public final void setHtmlEmail(final boolean htmlEmail) {
        this.htmlEmail = htmlEmail;
    }

    @Override
    public int compareTo(final Email email) {
        return email.received.compareTo(received);
    }

    @Override
    public boolean equals(final Object o) {
        if (o != null && o instanceof Email) {
            final Email email = (Email) o;
            return id.equals(email.getId());
        } else {
            return false;
        }
    }

}
