package io.kowalski;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import com.hubspot.dropwizard.guice.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.kowalski.claptrap.configuration.ClaptrapConfiguration;
import io.kowalski.claptrap.core.GuiceModule;
import io.kowalski.claptrap.core.StorageManager;
import io.kowalski.claptrap.exception.ClaptrapException;

public class Claptrap extends Application<ClaptrapConfiguration> {

    private final StorageManager storageManager;
    private final GuiceModule guiceModule;

    private Server jettyServer;

    private int httpPort;

    public Claptrap() {
        storageManager = new StorageManager();
        guiceModule = storageManager.getGuiceModule();
    }

    public static void main(final String[] args) throws ClaptrapException {
        try {
            new Claptrap().run(args);
        } catch (final Exception e) {
            throw new ClaptrapException("Claptrap was unable to start. Shutting down.", e);
        }
    }

    @Override
    public final void initialize(final Bootstrap<ClaptrapConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addBundle(GuiceBundle.<ClaptrapConfiguration> newBuilder()
                .addModule(guiceModule)
                .setConfigClass(ClaptrapConfiguration.class)
                .enableAutoConfig(getClass().getPackage().getName())
                .build());
    }

    @Override
    public final void run(final ClaptrapConfiguration configuration, final Environment environment) throws Exception, RuntimeException {
        environment.lifecycle().addServerLifecycleListener(new ServerLifecycleListener() {
            @Override
            public void serverStarted(final Server server) {
                jettyServer = server;
                for (final Connector connector : server.getConnectors()) {
                    if (connector instanceof ServerConnector) {
                        if ("application".equals(connector.getName())) {
                            httpPort = ((ServerConnector) connector).getLocalPort();
                        }
                    }
                }
            }
        });

        storageManager.startSMTPServer(configuration.getSmtpPort());
    }

    public final void shutdown() throws Exception {
        jettyServer.stop();
        storageManager.getHazelcast().shutdown();
        storageManager.getSMTPServer().stop();
    }

    public final int getHttpPort() {
        return httpPort;
    }

    public final int getSmtpPort() {
        return storageManager.getSMTPServer().getPort();
    }
}
