package io.kowalski.claptrap.storage.email;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;

import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.storage.AbstractStorageService;

public class EmailStorageService extends AbstractStorageService<Email, UUID> {

    private final static String MAP_NAME = "emails";

    @Inject
    public EmailStorageService(final HazelcastInstance hazelcast) {
        super(hazelcast, Email.class, MAP_NAME);
    }

    @Override
    public void store(final Email email) {
        fetchMap(MAP_NAME).set(email.getId(), email);
    }

    @Override
    public void store(final List<Email> emails) {
        for (final Email email : emails) {
            fetchMap(MAP_NAME).set(email.getId(), email);
        }
    }

    @Override
    public void remove(final Email email) {
        fetchMap(MAP_NAME).removeAsync(email.getId());
    }

    @Override
    public void remove(final List<Email> emails) {
        for (final Email email : emails) {
            remove(email);
        }
    }

    @Override
    public Collection<Email> retreive(final Predicate<?,?> predicate) {
        return fetchMap(MAP_NAME).values(predicate);
    }

    @Override
    public IMap<UUID, Email> getMap() {
        return fetchMap(MAP_NAME);
    }

}
