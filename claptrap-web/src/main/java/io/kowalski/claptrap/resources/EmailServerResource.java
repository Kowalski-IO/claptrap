package io.kowalski.claptrap.resources;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.hazelcast.mapreduce.aggregation.Aggregations;
import com.hazelcast.mapreduce.aggregation.Supplier;

import io.kowalski.claptrap.storage.email.EmailStorageService;

@Singleton
@Path("/servers")
@Produces(MediaType.APPLICATION_JSON)
public class EmailServerResource {

    private final EmailStorageService emailService;

    @Inject
    public EmailServerResource(final EmailStorageService emailService) {
        this.emailService = emailService;
    }

    @GET
    public Collection<String> allServers() {
        final Set<String> servers = emailService.getMap()
                .aggregate(Supplier.all(email -> email.getServerName()), Aggregations.distinctValues());
        return new TreeSet<String>(servers);
    }

}
