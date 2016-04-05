package io.kowalski.claptrap.storage;

import java.util.List;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseBroadcaster;

public interface BroadcastService<B, O> {

    void broadcast(B broadcastable);

    void broadcast(List<B> broadcastables);

    EventOutput generateEventOutput(O origin);

    SseBroadcaster fetchBroadcaster(O origin);

}
