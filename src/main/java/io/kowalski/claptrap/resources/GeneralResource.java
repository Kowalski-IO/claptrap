package io.kowalski.claptrap.resources;

import io.kowalski.claptrap.models.Contact;
import io.kowalski.claptrap.services.StorageService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class GeneralResource {

    @Inject
    private StorageService storageService;

    @GET
    @Path("contacts")
    public List<Contact> allContacts() {
        return storageService.allContacts();
    }

    @GET
    @Path("environments")
    public List<String> allEnvironments() {
        return storageService.allEnvironments();
    }

}
