package io.kowalski.claptrap.resources;

import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.models.PredicateMode;
import io.kowalski.claptrap.services.StorageService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Path("/emails")
@Produces(MediaType.APPLICATION_JSON)
public class EmailResource {

    @Inject
    private StorageService storageService;

    @GET
    public Collection<Email> emails(@QueryParam("environment") List<String> environment,
                                    @QueryParam("to") List<String> to, @QueryParam("cc") List<String> cc, @QueryParam("bcc") List<String> bcc,
                                    @QueryParam("from") List<String> from, @QueryParam("sender") List<String> sender, @QueryParam("reply_to") List<String> replyTo,
                                    @QueryParam("subject") List<String> subject, @QueryParam("body") List<String> body,
                                    @QueryParam("mode") PredicateMode mode) {
        List<Email> emails = storageService.retrieveForCriteria(environment, to, cc, bcc, from,
                sender, replyTo, subject, body, mode);
        Collections.sort(emails);
        return emails;
    }

    @DELETE
    public void deleteAllEmails() {
        storageService.deleteAllEmails();
    }

    @DELETE
    @Path("{id}")
    public void deleteEmail(@PathParam("id") UUID id) {
        storageService.deleteEmail(id);
    }

}
