package io.kowalski.claptrap.storage.logs;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;

import io.kowalski.claptrap.models.Log;
import io.kowalski.claptrap.storage.AbstractStorageService;
import io.kowalski.claptrap.storage.BroadcastService;

public class LogStorageService extends AbstractStorageService<Log, UUID> {

    private final static String MAP_NAME = "logs";

    private final BroadcastService<Log, String> broadcastService;

    @Inject
    public LogStorageService(final HazelcastInstance hazelcast, final BroadcastService<Log, String> broadcastService) {
        super(hazelcast, Log.class, MAP_NAME);
        this.broadcastService = broadcastService;
    }

    @Override
    public void store(final Log log) {
        fetchMap(MAP_NAME).set(log.getId(), log);
        broadcastService.broadcast(log);
    }

    @Override
    public void store(final List<Log> logs) {
        for (final Log log : logs) {
            store(log);
        }
    }

    @Override
    public void remove(final Log log) {
        fetchMap(MAP_NAME).removeAsync(log.getId());
    }

    @Override
    public void remove(final List<Log> logs) {
        for (final Log log : logs) {
            remove(log);
        }
    }

    @Override
    public Collection<Log> retreive(final Predicate<?, ?> predicate) {
        return fetchMap(MAP_NAME).values(predicate);
    }

    @Override
    public IMap<UUID, Log> getMap() {
        return fetchMap(MAP_NAME);
    }

}
