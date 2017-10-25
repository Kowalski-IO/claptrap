package io.kowalski.claptrap.resources;

import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.services.EmailStorage;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Path("/emails")
@Produces(MediaType.APPLICATION_JSON)
public class EmailResource {

    @Inject
    private EmailStorage emailStorage;

    @GET
    public Collection<Email> allEmails() {
        List<Email> emails = emailStorage.retrieveAll();
        Collections.sort(emails);
        return emails;
    }
//
//    @GET
//    @Path("/{environment}")
//    public Collection<Email> allEmailsForEnvironment(@PathParam("environment") final String environment) {
//        final Collection<Email> emails = emailStorage.retreive(new SqlPredicate("environment == ".concat(environment)));
//        Collections.sort(emails);
//        return emails;
//    }
//
//    @GET
//    @Path("/{environment}/{emailID}")
//    public Email singleEmailFromEnvironment(@PathParam("environment") final String environment, @PathParam("emailID") final UUID emailID) {
//        final String predicateString = "environment == ".concat(environment).concat(" AND id = ").concat(emailID.toString());
//        final List<Email> emails = Lists.newArrayList(storage.retreive(new SqlPredicate(predicateString)));
//        return emails.size() > 0 ? emails.get(0) : null;
//    }
//
//    @DELETE
//    @Path("/{environment}")
//    public void deleteEmailFromEnvironment(@PathParam("environment") final String environment) {
//        final String predicateString = "environment == ".concat(environment);
//        final List<Email> emails = Lists.newArrayList(storage.retreive(new SqlPredicate(predicateString)));
//        emailService.remove(emails);
//    }
//
//    @DELETE
//    @Path("/{environment}/{emailID}")
//    public Email deleteEmailFromEnvironment(@PathParam("environment") final String environment, @PathParam("emailID") final UUID emailID) {
//        final String predicateString = "environment == ".concat(environment).concat(" AND id = ").concat(emailID.toString());
//        final List<Email> emails = Lists.newArrayList(storage.retreive(new SqlPredicate(predicateString)));
//
//        final Email email = emails.size() > 0 ? emails.get(0) : null;
//
//        if (email != null) {
//            storage.remove(email);
//        }
//
//        return email;
//    }

}
