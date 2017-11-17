package io.kowalski.claptrap.resources;

import io.kowalski.claptrap.models.Attachment;
import io.kowalski.claptrap.models.Contact;
import io.kowalski.claptrap.services.StorageService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.List;
import java.util.UUID;

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

    @GET
    @Path("attachments/{email}/{attachment}")
    public Response fetchAttachment(@PathParam("email") UUID email, @PathParam("attachment") UUID attachment) {
        Attachment a = storageService.attachmentForId(email, attachment);
        File f = storageService.attachmentFileForId(email, attachment);

        String contentDisposition = "attachment; filename=\"".concat(a.getFilename()).concat("\"");

        return Response.ok(f)
                .header("Content-Disposition", contentDisposition)
                .header("Content-Type", MediaType.valueOf(a.getContentType()))
                .build();
    }

}
