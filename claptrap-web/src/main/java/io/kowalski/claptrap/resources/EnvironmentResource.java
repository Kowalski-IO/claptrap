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
import io.kowalski.claptrap.storage.logs.LogStorageService;

@Singleton
@Path("/environments")
@Produces(MediaType.APPLICATION_JSON)
public class EnvironmentResource {

    private final EmailStorageService emailService;
    private final LogStorageService logService;

    @Inject
    public EnvironmentResource(final EmailStorageService emailService, final LogStorageService logService) {
        this.emailService = emailService;
        this.logService = logService;
    }

    @GET
    public Collection<String> allEnvironments() {
        final Set<String> emailEnvironments = emailService.getMap()
                .aggregate(Supplier.all(email -> email.getEnvironment()), Aggregations.distinctValues());

        final Set<String> logEnvironments = logService.getMap().aggregate(Supplier.all(log -> log.getEnvironment()),
                Aggregations.distinctValues());

        final TreeSet<String> environments = new TreeSet<String>();
        environments.addAll(emailEnvironments);
        environments.addAll(logEnvironments);

        return environments;
    }

}
