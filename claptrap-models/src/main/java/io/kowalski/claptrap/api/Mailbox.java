package io.kowalski.claptrap.api;

import java.util.Collection;

import com.hazelcast.query.Predicate;

import io.kowalski.claptrap.exception.ClaptrapException;
import io.kowalski.claptrap.models.Email;

public interface Mailbox {

    /**
     * Fetch all of the emails stored by the targeted Claptrap instance.
     *
     * @return all of the emails stored in this instance.
     * @throws ClaptrapException if the emails could not be fetched or parsed.
     */
    Collection<Email> fetchAll() throws ClaptrapException;

    /**
     * Fetch all of the emails for a particular server stored by the targeted
     * Claptrap instance.
     *
     * @param serverName for the emails to fetch
     * @return all of the emails for a particular server stored in this
     *         instance.
     * @throws ClaptrapException if the emails could not be fetched or parsed.
     */
    Collection<Email> fetchAllForServer(final String serverName) throws ClaptrapException;

    /**
     * Fetches emails for a given predicate. Uses the Hazelcast predicate API
     * Full example: http://docs.hazelcast.org/docs/3.5/manual/html/querycriteriaapi.html
     *
     * @param predicate to filter emails on
     * @return the emails that match the given predicate
     * @throws ClaptrapException if the emails could not be fetched or parsed.
     */
    Collection<Email> fetchForCriteria(final Predicate<?, ?> predicate) throws ClaptrapException;
}
