package io.kowalski.claptrap.resources;

import io.kowalski.claptrap.services.ContactStorage;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("contacts")
@Produces(MediaType.APPLICATION_JSON)
public class ContactResouce {

    @Inject
    private ContactStorage contactStorage;

}
