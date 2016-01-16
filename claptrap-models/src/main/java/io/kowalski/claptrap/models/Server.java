package io.kowalski.claptrap.models;

import java.io.Serializable;

public class Server implements Serializable, Comparable<Server> {

    private static final long serialVersionUID = 5062874386030273410L;

    private String name;
    private long count;

    public Server(final String name) {
        this.name = name;
    }

    public Server(final String name, final long count) {
        this.name = name;
        this.count = count;
    }

    public Server() {

    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }


    public final long getCount() {
        return count;
    }

    public final void setCount(final long count) {
        this.count = count;
    }

    public int compareTo(final Server o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name;
    }

}
