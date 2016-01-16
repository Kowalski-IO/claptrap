package io.kowalski.claptrap.models;

public class SMS implements Message {

    private static final long serialVersionUID = -5430852687466352345L;

    private String from;
    private String to;
    private String message;

    public SMS() {

    }

    public final String getFrom() {
        return from;
    }

    public final void setFrom(final String from) {
        this.from = from;
    }

    public final String getTo() {
        return to;
    }

    public final void setTo(final String to) {
        this.to = to;
    }

    public final String getMessage() {
        return message;
    }

    public final void setMessage(final String message) {
        this.message = message;
    }

}
