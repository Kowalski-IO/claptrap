package io.kowalski.claptrap.storage.logs;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;

import io.kowalski.claptrap.models.Log;
import io.kowalski.claptrap.storage.BroadcastService;

public class LogBroadcastService implements BroadcastService<Log, String> {

    private final ConcurrentHashMap<String, SseBroadcaster> broadcasterMap;

    @Inject
    public LogBroadcastService(@Named("logBroadcastMap") final ConcurrentHashMap<String, SseBroadcaster> broadcasterMap) {
        this.broadcasterMap = broadcasterMap;
    }

    @Override
    public void broadcast(final Log log) {
        final SseBroadcaster broadcaster = fetchBroadcaster(log.getEnvironment());

        final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        final OutboundEvent event = eventBuilder.name("message").mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(Log.class, log).build();

        broadcaster.broadcast(event);
    }

    @Override
    public void broadcast(final List<Log> logs) {
        for (final Log log : logs) {
            broadcast(log);
        }
    }

    @Override
    public EventOutput generateEventOutput(final String environmentName) {
        final SseBroadcaster broadcaster = fetchBroadcaster(environmentName);
        final EventOutput eventOutput = new EventOutput();
        broadcaster.add(eventOutput);
        return eventOutput;
    }

    @Override
    public SseBroadcaster fetchBroadcaster(final String environmentName) {
        final Optional<SseBroadcaster> optionalBroadcaster =
                Optional.ofNullable(broadcasterMap.get(environmentName));

        SseBroadcaster broadcaster;

        if (!optionalBroadcaster.isPresent()) {
            broadcaster = new SseBroadcaster();
            broadcasterMap.put(environmentName, broadcaster);
        } else {
            broadcaster = optionalBroadcaster.get();
        }

        return broadcaster;
    }

}
