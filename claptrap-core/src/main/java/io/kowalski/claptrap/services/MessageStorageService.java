package io.kowalski.claptrap.services;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import io.kowalski.claptrap.models.Message;
import io.kowalski.claptrap.models.Server;

public interface MessageStorageService<T extends Message> {

    void store(T message);

    Map<UUID, T> fetchFromServer(String serverName);

    T fetchFromServerById(String serverName, UUID id);

    T deleteFromServer(String serverName, UUID id);

    void emptyServer(String serverName);

    Set<Server> fetchServers();

    Set<Server> fetchServersWithCounts();

}
