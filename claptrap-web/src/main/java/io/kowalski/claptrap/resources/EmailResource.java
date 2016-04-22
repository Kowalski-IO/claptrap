package io.kowalski.claptrap.resources;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.SerializationUtils;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseFeature;

import com.google.inject.Inject;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.SqlPredicate;

import io.kowalski.claptrap.api.PredicateWrapper;
import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.storage.BroadcastService;
import io.kowalski.claptrap.storage.email.EmailStorageService;
import jersey.repackaged.com.google.common.collect.Lists;

@Singleton
@Path("/emails")
@Produces(MediaType.APPLICATION_JSON)
public class EmailResource {

    private final EmailStorageService emailService;
    private final BroadcastService<Email, String> broadcastService;

    @Inject
    public EmailResource(final EmailStorageService emailService, final BroadcastService<Email, String> broadcastService) {
        this.emailService = emailService;
        this.broadcastService = broadcastService;
    }

    @GET
    public Collection<Email> allEmails() {
        final List<Email> emails = Lists.newArrayList(emailService.retreive(new SqlPredicate("id != null")));
        Collections.sort(emails);
        return emails;
    }

    @POST
    public Collection<Email> allEmailsForPredicate(final PredicateWrapper predicateWrapper) {
        final Predicate<?, ?> predicate = SerializationUtils.deserialize(predicateWrapper.getData());
        final List<Email> emails = Lists.newArrayList(emailService.retreive(predicate));
        Collections.sort(emails);
        return emails;
    }

    @GET
    @Path("/{environment}")
    public Collection<Email> allEmailsForEnvironment(@PathParam("environment") final String environment) {
        final List<Email> emails = Lists.newArrayList(emailService.retreive(new SqlPredicate("environment == ".concat(environment))));
        Collections.sort(emails);
        return emails;
    }

    @GET
    @Path("/{environment}/{emailID}")
    public Email singleEmailFromEnvironment(@PathParam("environment") final String environment, @PathParam("emailID") final UUID emailID) {
        final String predicateString = "environment == ".concat(environment).concat(" AND id = ").concat(emailID.toString());
        final List<Email> emails = Lists.newArrayList(emailService.retreive(new SqlPredicate(predicateString)));
        return emails.size() > 0 ? emails.get(0) : null;
    }

    @DELETE
    @Path("/{environment}")
    public void deleteEmailFromEnvironment(@PathParam("environment") final String environment) {
        final String predicateString = "environment == ".concat(environment);
        final List<Email> emails = Lists.newArrayList(emailService.retreive(new SqlPredicate(predicateString)));
        emailService.remove(emails);
    }

    @DELETE
    @Path("/{environment}/{emailID}")
    public Email deleteEmailFromEnvironment(@PathParam("environment") final String environment, @PathParam("emailID") final UUID emailID) {
        final String predicateString = "environment == ".concat(environment).concat(" AND id = ").concat(emailID.toString());
        final List<Email> emails = Lists.newArrayList(emailService.retreive(new SqlPredicate(predicateString)));

        final Email email = emails.size() > 0 ? emails.get(0) : null;

        if (email != null) {
            emailService.remove(email);
        }

        return email;
    }

    @GET
    @Path("/broadcast/{environment}")
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput listenToBroadcast(@PathParam("environment") final String environment) {
        return broadcastService.generateEventOutput(environment);
    }

}
