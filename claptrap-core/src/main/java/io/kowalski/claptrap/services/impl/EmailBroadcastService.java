package io.kowalski.claptrap.services.impl;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;

import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.models.Server;
import io.kowalski.claptrap.services.BroadcastService;

public class EmailBroadcastService implements BroadcastService<Email> {

    private final ConcurrentHashMap<String, SseBroadcaster> broadcasterMap;

    @Inject
    public EmailBroadcastService(final ConcurrentHashMap<String, SseBroadcaster> broadcasterMap) {
        this.broadcasterMap = broadcasterMap;
    }

    @Override
    public void broadcast(final Email email) {
        final SseBroadcaster broadcaster = fetchBroadcaster(email.getServer());

        final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        final OutboundEvent event = eventBuilder.name("message")
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(Email.class, email)
                .build();

        broadcaster.broadcast(event);

    }

    @Override
    public EventOutput generateEventOutput(final Server server) {
        final SseBroadcaster broadcaster = fetchBroadcaster(server);
        final EventOutput eventOutput = new EventOutput();
        broadcaster.add(eventOutput);
        return eventOutput;
    }

    @Override
    public SseBroadcaster fetchBroadcaster(final Server server) {
        final String serverName = server.getName();
        final Optional<SseBroadcaster> optionalBroadcaster =
                Optional.ofNullable(broadcasterMap.get(serverName));

        SseBroadcaster broadcaster;

        if (!optionalBroadcaster.isPresent()) {
            broadcaster = new SseBroadcaster();
            broadcasterMap.put(serverName, broadcaster);
        } else {
            broadcaster = optionalBroadcaster.get();
        }

        return broadcaster;
    }

}
