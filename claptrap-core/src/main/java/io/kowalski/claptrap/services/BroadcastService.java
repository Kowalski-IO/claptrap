package io.kowalski.claptrap.services;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseBroadcaster;

import io.kowalski.claptrap.models.Message;
import io.kowalski.claptrap.models.Server;

public interface BroadcastService<T extends Message> {

    void broadcast(T message);

    EventOutput generateEventOutput(Server server);

    SseBroadcaster fetchBroadcaster(Server server);

}
