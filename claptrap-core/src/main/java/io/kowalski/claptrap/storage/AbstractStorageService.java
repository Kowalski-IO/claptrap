package io.kowalski.claptrap.storage;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;

import io.kowalski.claptrap.models.annotations.Indexable;

public abstract class AbstractStorageService<T, I> {

    protected final HazelcastInstance hazelcast;

    public AbstractStorageService(final HazelcastInstance hazelcast, final Class<?> clazz, final String mapName) {
        this.hazelcast = hazelcast;
        bootstrap(clazz, mapName);
    }

    private void bootstrap(final Class<?> clazz, final String mapName) {
        final IMap<I, T> map = hazelcast.getMap(mapName);
        for(final Field field : clazz.getDeclaredFields()){
            final Indexable indexable = field.getAnnotation(Indexable.class);
            if (indexable != null) {
                map.addIndex(field.getName(), true );
            }
        }
    }

    protected IMap<I, T> fetchMap(final String mapName) {
        return hazelcast.getMap(mapName);
    }

    protected abstract void store(T item);

    protected abstract void store(List<T> items);

    protected abstract void remove(T item);

    protected abstract void remove(List<T> items);

    protected abstract Collection<T> retreive(final Predicate<?,?> predicate);

    protected abstract IMap<I, T> getMap();

}
