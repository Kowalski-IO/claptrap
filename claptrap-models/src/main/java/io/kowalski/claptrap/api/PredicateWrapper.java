package io.kowalski.claptrap.api;

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;

import com.hazelcast.query.Predicate;

public class PredicateWrapper implements Serializable {

    private static final long serialVersionUID = -5804754984350865515L;

    private String className;
    private byte[] data;

    public PredicateWrapper() {

    }

    public PredicateWrapper(final Predicate<?, ?> predicate) {
        className = predicate.getClass().getName();
        data = SerializationUtils.serialize(predicate);
    }

    public final String getClassName() {
        return className;
    }

    public final void setClassName(final String className) {
        this.className = className;
    }

    public final byte[] getData() {
        return data;
    }

    public final void setData(final byte[] data) {
        this.data = data;
    }

}
