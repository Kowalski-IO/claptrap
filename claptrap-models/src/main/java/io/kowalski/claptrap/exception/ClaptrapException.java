package io.kowalski.claptrap.exception;

public class ClaptrapException extends Exception {

    private static final long serialVersionUID = -2714265807511515481L;

    public ClaptrapException() {

    }

    public ClaptrapException(final String message) {
        super(message);
    }

    public ClaptrapException(final Throwable cause) {
        super(cause);
    }

    public ClaptrapException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ClaptrapException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
