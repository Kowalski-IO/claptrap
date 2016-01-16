package io.kowalski.claptrap.services.impl;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import javax.inject.Inject;

import org.mapdb.DB;
import org.mapdb.TxMaker;

import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.models.Server;
import io.kowalski.claptrap.services.MessageStorageService;

public class EmailStorageService implements MessageStorageService<Email> {

    private final TxMaker txMaker;
    private final EmailBroadcastService broadcastService;
    private final String SERVER_SET_NAME = "ServerSet";

    @Inject
    public EmailStorageService(final TxMaker txMaker, final EmailBroadcastService broadcastService) {
        this.txMaker = txMaker;
        this.broadcastService = broadcastService;
    }

    @Override
    public void store(final Email email) {
        final DB transaction = txMaker.makeTx();
        final Map<UUID, Email> map = transaction.treeMap(email.getServer().getName());
        map.put(email.getId(), email);

        final Set<Server> servers = transaction.treeSet(SERVER_SET_NAME);
        servers.add(email.getServer());

        transaction.commit();
        transaction.close();

        broadcastService.broadcast(email);
    }

    @Override
    public Map<UUID, Email> fetchFromServer(final String serverName) {
        final Map<UUID, Email> tempMap = new TreeMap<UUID, Email>();

        final DB transaction = txMaker.makeTx();
        final Map<UUID, Email> map = transaction.treeMap(serverName);
        tempMap.putAll(map);
        transaction.close();

        return tempMap;
    }

    @Override
    public Email fetchFromServerById(final String serverName, final UUID id) {
        Email tempEmail;

        final DB transaction = txMaker.makeTx();
        final Map<UUID, Email> map = transaction.treeMap(serverName);
        tempEmail = map.get(id);
        transaction.close();

        return tempEmail;
    }

    @Override
    public Email deleteFromServer(final String serverName, final UUID id) {
        Email tempEmail;

        final DB transaction = txMaker.makeTx();
        final Map<UUID, Email> map = transaction.treeMap(serverName);
        tempEmail = map.remove(id);
        transaction.commit();
        transaction.close();

        return tempEmail;
    }

    @Override
    public void emptyServer(final String serverName) {
        final DB transaction = txMaker.makeTx();
        final Map<UUID, Email> map = transaction.treeMap(serverName);
        map.clear();
        transaction.commit();
        transaction.close();
    }

    @Override
    public Set<Server> fetchServers() {
        final Set<Server> tempSet = new TreeSet<Server>();

        final DB transaction = txMaker.makeTx();
        final Set<Server> servers = transaction.treeSet(SERVER_SET_NAME);
        tempSet.addAll(servers);

        transaction.close();

        return tempSet;
    }

    @Override
    public Set<Server> fetchServersWithCounts() {
        final Set<Server> tempSet = new TreeSet<Server>();

        final Set<Server> servers = fetchServers();

        for (final Server server : servers) {
            final DB transaction = txMaker.makeTx();
            final Map<UUID, Email> currentServerMap = transaction.treeMap(server.getName());
            tempSet.add(new Server(server.getName(), currentServerMap.size()));
            transaction.close();
        }

        return tempSet;
    }

}
