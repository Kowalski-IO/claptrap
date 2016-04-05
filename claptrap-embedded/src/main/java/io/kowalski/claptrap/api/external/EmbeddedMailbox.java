package io.kowalski.claptrap.api.external;

import java.util.Collection;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.SqlPredicate;

import io.kowalski.claptrap.api.Mailbox;
import io.kowalski.claptrap.core.StorageManager;
import io.kowalski.claptrap.exception.ClaptrapException;
import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.storage.email.EmailStorageService;

public class EmbeddedMailbox implements Mailbox {

    private final StorageManager storageManager;
    private final EmailStorageService emailStorageService;

    /**
     * Constructs a new mailbox instance in memory.
     */
    public EmbeddedMailbox() {
        storageManager = new StorageManager();
        storageManager.startSMTPServer();
        emailStorageService = storageManager.getInjector().getInstance(EmailStorageService.class);
    }

    /**
     * Fetch all of the emails stored by the embedded Claptrap instance.
     *
     * @return all of the emails stored in this instance.
     * @throws ClaptrapException if the emails could not be fetched or parsed.
     */
    @Override
    public Collection<Email> fetchAll() throws ClaptrapException {
        return emailStorageService.retreive((new SqlPredicate("id != null")));
    }

    /**
     * Fetch all of the emails for a particular server stored by the embedded
     * Claptrap instance.
     *
     * @param serverName for the emails to fetch
     * @return all of the emails for a particular server stored in this
     *         instance.
     * @throws ClaptrapException if the emails could not be fetched or parsed.
     */
    @Override
    public Collection<Email> fetchAllForServer(final String serverName) throws ClaptrapException {
        return emailStorageService.retreive((new SqlPredicate("serverName == ".concat(serverName))));
    }

    /**
     * Fetches emails for a given predicate. Uses the Hazelcast predicate API
     * Full example:
     * http://docs.hazelcast.org/docs/3.5/manual/html/querycriteriaapi.html
     *
     * @param predicate to filter emails on
     * @return the emails that match the given predicate
     * @throws ClaptrapException if the emails could not be fetched or parsed.
     */
    @Override
    public Collection<Email> fetchForCriteria(final Predicate<?, ?> predicate) throws ClaptrapException {
        return emailStorageService.retreive(predicate);
    }

    public int getSMTPPort() {
        return storageManager.getSMTPServer().getPort();
    }

    public void close() {
        if (storageManager != null) {
            storageManager.getHazelcast().shutdown();
            storageManager.getSMTPServer().stop();
        }
    }

}
