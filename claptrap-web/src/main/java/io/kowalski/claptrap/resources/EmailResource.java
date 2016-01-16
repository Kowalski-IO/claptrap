package io.kowalski.claptrap.resources;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;

import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.models.Server;
import io.kowalski.claptrap.services.impl.EmailStorageService;

@Singleton
@Path("/emails")
@Produces(MediaType.APPLICATION_JSON)
public class EmailResource {

    private final EmailStorageService emailService;

    @Inject
    public EmailResource(final EmailStorageService emailService) {
        this.emailService = emailService;
    }

    @GET
    public Map<Server, Map<UUID, Email>> allEmails() {
        final Map<Server, Map<UUID, Email>> allEmails = new TreeMap<Server, Map<UUID,Email>>();
        final Set<Server> servers = emailService.fetchServers();

        for (final Server server : servers) {
            allEmails.put(server, emailService.fetchFromServer(server.getName()));
        }
        return allEmails;
    }

}
