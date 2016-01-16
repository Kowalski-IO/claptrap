package io.kowalski.claptrap.resources;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.models.Server;
import io.kowalski.claptrap.services.impl.EmailStorageService;

@Singleton
@Path("/servers")
@Produces(MediaType.APPLICATION_JSON)
public class ServerResource {


    private final EmailStorageService emailService;

    @Inject
    public ServerResource(final EmailStorageService emailService) {
        this.emailService = emailService;
    }

    @GET
    public Set<Server> fetchAllServers() {
        return emailService.fetchServers();
    }

    @GET
    @Path("/{serverName}/emails")
    public Collection<Email> allEmailsForServer(@PathParam("serverName") final String serverName) {
        final List<Email> emails = Lists.newArrayList(emailService.fetchFromServer(serverName).values());
        Collections.sort(emails);
        return emails;
    }

    @DELETE
    @Path("/{serverName}/emails")
    public void deleteAllEmailsForServer(@PathParam("serverName") final String serverName) {
        emailService.emptyServer(serverName);
    }

    @GET
    @Path("/{serverName}/emails/{emailID}")
    public Email fetchEmailFromServer(@PathParam("serverName") final String serverName,
            @PathParam("emailID") final UUID id) {
        return emailService.fetchFromServerById(serverName, id);
    }

    @DELETE
    @Path("/{serverName}/emails/{emailID}")
    public Email deleteEmailFromServer(@PathParam("serverName") final String serverName,
            @PathParam("emailID") final UUID id) {
        return emailService.deleteFromServer(serverName, id);
    }

}
