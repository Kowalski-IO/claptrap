package io.kowalski.claptrap.services;

import io.kowalski.claptrap.models.Message;

public interface Parser<P extends Object, T extends Message> extends Runnable {

    T parseMessage (final P parsable) throws Exception;

}
