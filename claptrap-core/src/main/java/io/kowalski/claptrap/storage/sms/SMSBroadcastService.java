package io.kowalski.claptrap.storage.sms;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;

import io.kowalski.claptrap.models.SMS;
import io.kowalski.claptrap.storage.BroadcastService;

public class SMSBroadcastService implements BroadcastService<SMS, String> {

    private final ConcurrentHashMap<String, SseBroadcaster> broadcasterMap;

    @Inject
    public SMSBroadcastService(final ConcurrentHashMap<String, SseBroadcaster> broadcasterMap) {
        this.broadcasterMap = broadcasterMap;
    }

    @Override
    public void broadcast(final SMS sms) {
        final SseBroadcaster broadcaster = fetchBroadcaster(sms.getTo());

        final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        final OutboundEvent event = eventBuilder.name("message")
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(SMS.class, sms)
                .build();

        broadcaster.broadcast(event);
    }

    @Override
    public void broadcast(final List<SMS> broadcastables) {
        for (final SMS sms : broadcastables) {
            broadcast(sms);
        }
    }

    @Override
    public EventOutput generateEventOutput(final String phoneNumber) {
        final SseBroadcaster broadcaster = fetchBroadcaster(phoneNumber);
        final EventOutput eventOutput = new EventOutput();
        broadcaster.add(eventOutput);
        return eventOutput;
    }

    @Override
    public SseBroadcaster fetchBroadcaster(final String phoneNumber) {
        final Optional<SseBroadcaster> optionalBroadcaster =
                Optional.ofNullable(broadcasterMap.get(phoneNumber));

        SseBroadcaster broadcaster;

        if (!optionalBroadcaster.isPresent()) {
            broadcaster = new SseBroadcaster();
            broadcasterMap.put(phoneNumber, broadcaster);
        } else {
            broadcaster = optionalBroadcaster.get();
        }

        return broadcaster;
    }

}
