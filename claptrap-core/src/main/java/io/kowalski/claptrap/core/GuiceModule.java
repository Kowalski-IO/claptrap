package io.kowalski.claptrap.core;

import java.util.concurrent.ConcurrentHashMap;

import org.glassfish.jersey.media.sse.SseBroadcaster;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.models.Log;
import io.kowalski.claptrap.models.SMS;
import io.kowalski.claptrap.storage.BroadcastService;
import io.kowalski.claptrap.storage.email.EmailBroadcastService;
import io.kowalski.claptrap.storage.logs.LogBroadcastService;
import io.kowalski.claptrap.storage.sms.SMSBroadcastService;

public class GuiceModule extends AbstractModule {

    private final HazelcastInstance hazelcast;
    private final ConcurrentHashMap<String, SseBroadcaster> emailBroadcasterMap;
    private final ConcurrentHashMap<String, SseBroadcaster> logBroadcasterMap;

    public GuiceModule() {
        final Config config = new Config();
        config.setProperty("hazelcast.logging.type", "none");

        final NetworkConfig network = config.getNetworkConfig();

        final JoinConfig join = network.getJoin();
        join.getTcpIpConfig().setEnabled(false);
        join.getAwsConfig().setEnabled(false);
        join.getMulticastConfig().setEnabled(false);

        hazelcast = Hazelcast.newHazelcastInstance(config);

        hazelcast.getConfig().setProperty("hazelcast.logging.type", "slf4j");

        emailBroadcasterMap = new ConcurrentHashMap<String, SseBroadcaster>();
        logBroadcasterMap = new ConcurrentHashMap<String, SseBroadcaster>();
    }

    @Override
    protected void configure() {
        bind(HazelcastInstance.class).toInstance(hazelcast);
        bind(new TypeLiteral<ConcurrentHashMap<String, SseBroadcaster>>() {
        }).annotatedWith(Names.named("emailBroadcastMap")).toInstance(emailBroadcasterMap);
        bind(new TypeLiteral<ConcurrentHashMap<String, SseBroadcaster>>() {
        }).annotatedWith(Names.named("logBroadcastMap")).toInstance(logBroadcasterMap);
        bind(new TypeLiteral<BroadcastService<Email, String>>(){}).to(EmailBroadcastService.class);
        bind(new TypeLiteral<BroadcastService<Log, String>>(){}).to(LogBroadcastService.class);
        bind(new TypeLiteral<BroadcastService<SMS, String>>(){}).to(SMSBroadcastService.class);
    }

    public final HazelcastInstance getHazelcast() {
        return hazelcast;
    }

    public final ConcurrentHashMap<String, SseBroadcaster> getBroadcasterMap() {
        return emailBroadcasterMap;
    }

}
