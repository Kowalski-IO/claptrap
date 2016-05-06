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

import com.google.inject.Inject;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.SqlPredicate;

import io.kowalski.claptrap.api.PredicateWrapper;
import io.kowalski.claptrap.models.Log;
import io.kowalski.claptrap.storage.logs.LogStorageService;
import jersey.repackaged.com.google.common.collect.Lists;

@Singleton
@Path("/logs")
@Produces(MediaType.APPLICATION_JSON)
public class LogResource {

    private final LogStorageService logService;

    @Inject
    public LogResource(final LogStorageService logService) {
        this.logService = logService;
    }

    @GET
    public Collection<Log> allLogs() {
        final List<Log> logs = Lists.newArrayList(logService.retreive(new SqlPredicate("id != null")));
        Collections.sort(logs);
        return logs;
    }

    @POST
    public Log storeLog(final Log log) {
        logService.store(log);
        return log;
    }

    @POST
    @Path("/filter")
    public Collection<Log> allLogsForPredicate(final PredicateWrapper predicateWrapper) {
        final Predicate<?, ?> predicate = SerializationUtils.deserialize(predicateWrapper.getData());
        final List<Log> logs = Lists.newArrayList(logService.retreive(predicate));
        Collections.sort(logs);
        return logs;
    }

    @GET
    @Path("/{environment}")
    public Collection<Log> allLogsForEnvironment(@PathParam("environment") final String environment) {
        final List<Log> logs = Lists.newArrayList(logService.retreive(new SqlPredicate("environment == ".concat(environment))));
        Collections.sort(logs);
        return logs;
    }

    @GET
    @Path("/{environment}/{logID}")
    public Log singleLogFromEnvironment(@PathParam("environment") final String environment, @PathParam("logID") final UUID logID) {
        final String predicateString = "environment == ".concat(environment).concat(" AND id = ").concat(logID.toString());
        final List<Log> logs = Lists.newArrayList(logService.retreive(new SqlPredicate(predicateString)));
        return logs.size() > 0 ? logs.get(0) : null;
    }

    @DELETE
    @Path("/{environment}")
    public void deleteLogsFromEnvironment(@PathParam("environment") final String environment) {
        final String predicateString = "environment == ".concat(environment);
        final List<Log> logs = Lists.newArrayList(logService.retreive(new SqlPredicate(predicateString)));
        logService.remove(logs);
    }

    @DELETE
    @Path("/{environment}/{logID}")
    public Log deleteLogFromEnvironment(@PathParam("environment") final String environment, @PathParam("logID") final UUID logID) {
        final String predicateString = "environment == ".concat(environment).concat(" AND id = ").concat(logID.toString());
        final List<Log> logs = Lists.newArrayList(logService.retreive(new SqlPredicate(predicateString)));

        final Log log = logs.size() > 0 ? logs.get(0) : null;

        if (log != null) {
            logService.remove(log);
        }

        return log;
    }

}
