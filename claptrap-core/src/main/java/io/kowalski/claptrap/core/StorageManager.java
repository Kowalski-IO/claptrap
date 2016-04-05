package io.kowalski.claptrap.core;

import java.util.concurrent.ConcurrentHashMap;

import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.server.SMTPServer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hazelcast.core.HazelcastInstance;

import io.kowalski.claptrap.handler.SMTPMessageHandlerFactory;

/**
 * @author Brandon Kowalski
 * @since 2.2.0
 */
public class StorageManager {

    private final GuiceModule module;
    private final Injector injector;
    private final SMTPServer smtpServer;

    public StorageManager() {
        module = new GuiceModule();
        injector = Guice.createInjector(module);

        final MessageHandlerFactory smhf = injector.getInstance(SMTPMessageHandlerFactory.class);

        smtpServer = new SMTPServer(smhf);
        smtpServer.setSoftwareName("Claptrap SMTP");
    }

    public void startSMTPServer(final int port) {
        smtpServer.setPort(port);
        smtpServer.start();
    }

    public void startSMTPServer() {
        this.startSMTPServer(0);
    }

    public final GuiceModule getGuiceModule() {
        return module;
    }

    public final HazelcastInstance getHazelcast() {
        return module.getHazelcast();
    }

    public final ConcurrentHashMap<String, SseBroadcaster> getBroadcasterMap() {
        return module.getBroadcasterMap();
    }

    public final SMTPServer getSMTPServer() {
        return smtpServer;
    }

    public final Injector getInjector() {
        return injector;
    }

}
