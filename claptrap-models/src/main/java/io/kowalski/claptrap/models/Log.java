package io.kowalski.claptrap.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.kowalski.claptrap.models.annotations.Indexable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Log implements Serializable, Comparable<Log> {

    private static final long serialVersionUID = -4851792934883908412L;

    @Indexable
    private final UUID id;

    @Indexable
    @JsonProperty
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd' @ 'hh:mm:ss a")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime received;

    @Indexable
    private String environment;

    @Indexable
    private String level;

    private String message;
    private Map<String, String> propertyMap;

    private Map<String, Object> throwable;

    @Indexable
    private String throwableClass;

    public Log() {
        id = UUID.randomUUID();
        received = LocalDateTime.now();
    }

    public final String getEnvironment() {
        return environment;
    }

    public final void setEnvironment(final String environment) {
        this.environment = environment;
    }

    public final String getLevel() {
        return level;
    }

    public final void setLevel(final String level) {
        this.level = level;
    }

    public final String getMessage() {
        return message;
    }

    public final void setMessage(final String message) {
        this.message = message;
    }

    public final Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    public final void setPropertyMap(final Map<String, String> propertyMap) {
        this.propertyMap = propertyMap;
    }

    public final Map<String, Object> getThrowable() {
        return throwable;
    }

    public final void setThrowable(final Map<String, Object> throwable) {
        this.throwable = throwable;
    }

    public final String getThrowableClass() {
        return throwableClass;
    }

    public final void setThrowableClass(final String throwableClass) {
        this.throwableClass = throwableClass;
    }

    public final UUID getId() {
        return id;
    }

    public final LocalDateTime getReceived() {
        return received;
    }

    @Override
    public int compareTo(final Log log) {
        return log.received.compareTo(received);
    }

    @Override
    public boolean equals(final Object o) {
        if (o != null && o instanceof Log) {
            final Log log = (Log) o;
            return id.equals(log.getId());
        } else {
            return false;
        }
    }

}

