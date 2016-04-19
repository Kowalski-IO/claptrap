package io.kowalski.claptrap.storage.logs;

import java.util.List;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseBroadcaster;

import io.kowalski.claptrap.models.Log;
import io.kowalski.claptrap.storage.BroadcastService;

public class LogBroadcastService implements BroadcastService<Log, String> {

    @Override
    public void broadcast(final Log broadcastable) {
        // TODO Auto-generated method stub

    }

    @Override
    public void broadcast(final List<Log> broadcastables) {
        // TODO Auto-generated method stub

    }

    @Override
    public EventOutput generateEventOutput(final String origin) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SseBroadcaster fetchBroadcaster(final String origin) {
        // TODO Auto-generated method stub
        return null;
    }

}
