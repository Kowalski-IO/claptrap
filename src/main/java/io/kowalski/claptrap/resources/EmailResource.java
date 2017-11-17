package io.kowalski.claptrap.resources;

import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.models.filters.Filter;
import io.kowalski.claptrap.services.FilterService;
import io.kowalski.claptrap.services.StorageService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/emails")
@Produces(MediaType.APPLICATION_JSON)
public class EmailResource {

    @Inject
    private StorageService storageService;

    @GET
    public Collection<Email> emails(@QueryParam("environment") List<String> environment) {
        return storageService.retrieveForEnvironment(environment);
    }

    @POST
    public boolean emailsForFilter(@QueryParam("environment") List<String> environment, final Map<String, Object> jsonFilter) {

        Filter f = FilterService.parseJSON(jsonFilter);

        return true;
    }

    @GET
    @Path("{id}/plain")
    @Produces(MediaType.TEXT_PLAIN)
    public String bodyPlain(@PathParam("id") UUID id) {
        return storageService.plainBodyForId(id);
    }

    @GET
    @Path("{id}/html")
    @Produces(MediaType.TEXT_HTML)
    public String bodyHTML(@PathParam("id") UUID id) {
        return storageService.htmlBodyForId(id);
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
