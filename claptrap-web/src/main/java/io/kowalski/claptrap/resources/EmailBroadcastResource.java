package io.kowalski.claptrap.resources;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseFeature;

import com.google.inject.Inject;

import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.models.Server;
import io.kowalski.claptrap.services.BroadcastService;

@Singleton
@Path("/broadcast/email")
public class EmailBroadcastResource {

    private final BroadcastService<Email> broadcastService;

    @Inject
    public EmailBroadcastResource(final BroadcastService<Email> broadcastService) {
        this.broadcastService = broadcastService;
    }

    @GET
    @Path("/{serverName}")
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput listenToBroadcast(@PathParam("serverName") final String serverName) {
        return broadcastService.generateEventOutput(new Server(serverName));
    }

}
