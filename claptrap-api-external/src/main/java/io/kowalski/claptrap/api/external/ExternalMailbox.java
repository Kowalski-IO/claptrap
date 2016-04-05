package io.kowalski.claptrap.api.external;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.hazelcast.query.Predicate;

import io.kowalski.claptrap.api.Mailbox;
import io.kowalski.claptrap.api.PredicateWrapper;
import io.kowalski.claptrap.exception.ClaptrapException;
import io.kowalski.claptrap.models.Email;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Mailbox is the entry point for Claptrap email intercepting within a Java
 * application.
 *
 * @author Brandon Kowalski
 * @since 1.0.0
 *
 */
public class ExternalMailbox implements Mailbox {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final OkHttpClient okhttp;
    private static final ObjectMapper mapper;
    private static final TypeFactory typeFactory;

    static {
        okhttp = new OkHttpClient();
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        typeFactory = mapper.getTypeFactory();
    }

    private final String instanceURL;

    /**
     * Constructs a new mailbox instance that hooks into an existing Claptrap
     * installation. (i.e. one developers and testers use through the web UI)
     *
     * @param instanceURL pointing to the Claptrap (i.e. the URL that loads the
     *        UI homepage)
     */
    public ExternalMailbox(final String instanceURL) {
        this.instanceURL = instanceURL;
    }

    /**
     * Fetch all of the emails stored by the targeted Claptrap instance.
     *
     * @return all of the emails stored in this instance.
     * @throws ClaptrapException if the emails could not be fetched or parsed.
     */
    @Override
    public Collection<Email> fetchAll() throws ClaptrapException {
        try {
            final List<Email> result = new ArrayList<Email>();
            final Request request = new Request.Builder().url(instanceURL.concat("/api/emails")).build();
            final Response response = okhttp.newCall(request).execute();
            final List<Email> parsedEmails = mapper.readValue(response.body().string(),
                    typeFactory.constructCollectionType(List.class, Email.class));

            if (parsedEmails != null) {
                result.addAll(parsedEmails);
            }

            return result;
        } catch (final IOException e) {
            throw new ClaptrapException("Unable to fetch all emails from targeted Claptrap instance.", e);
        }
    }

    /**
     * Fetch all of the emails for a particular server stored by the targeted
     * Claptrap instance.
     *
     * @param serverName for the emails to fetch
     * @return all of the emails for a particular server stored in this
     *         instance.
     * @throws ClaptrapException if the emails could not be fetched or parsed.
     */
    @Override
    public Collection<Email> fetchAllForServer(final String serverName) throws ClaptrapException {
        try {
            final List<Email> result = new ArrayList<Email>();
            final Request request = new Request.Builder().url(instanceURL.concat("/api/emails/").concat(serverName))
                    .build();
            final Response response = okhttp.newCall(request).execute();
            final List<Email> parsedEmails = mapper.readValue(response.body().string(),
                    typeFactory.constructCollectionType(List.class, Email.class));

            if (parsedEmails != null) {
                result.addAll(parsedEmails);
            }

            return result;
        } catch (final IOException e) {
            throw new ClaptrapException("Unable to fetch all emails from targeted Claptrap instance.", e);
        }
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
        try {
            final List<Email> result = new ArrayList<Email>();

            final PredicateWrapper wrapper = new PredicateWrapper(predicate);

            final RequestBody body = RequestBody.create(JSON, mapper.writeValueAsString(wrapper));
            final Request request = new Request.Builder().url(instanceURL.concat("/api/emails")).post(body).build();
            final Response response = okhttp.newCall(request).execute();

            final List<Email> parsedEmails = mapper.readValue(response.body().string(),
                    typeFactory.constructCollectionType(List.class, Email.class));

            if (parsedEmails != null) {
                result.addAll(parsedEmails);
            }

            return result;
        } catch (final IOException e) {
            throw new ClaptrapException("Unable to fetch all emails from targeted Claptrap instance.", e);
        }
    }

}
