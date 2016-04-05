package io.kowalski.claptrap.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class SMS implements Serializable {

    private static final long serialVersionUID = -5430852687466352345L;

    private UUID id;

    private String to;
    private String from;
    private LocalDateTime received;
    private String message;

    public SMS() {
    }

    public final UUID getId() {
        return id;
    }

    public final void setId(final UUID id) {
        this.id = id;
    }

    public final String getTo() {
        return to;
    }

    public final void setTo(final String to) {
        this.to = to;
    }

    public final String getFrom() {
        return from;
    }

    public final void setFrom(final String from) {
        this.from = from;
    }

    public final LocalDateTime getReceived() {
        return received;
    }

    public final void setReceived(final LocalDateTime received) {
        this.received = received;
    }

    public final String getMessage() {
        return message;
    }

    public final void setMessage(final String message) {
        this.message = message;
    }

}
