package io.kowalski;

import com.hubspot.dropwizard.guice.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.kowalski.claptrap.configuration.ClaptrapConfiguration;
import io.kowalski.claptrap.core.GuiceModule;
import io.kowalski.claptrap.core.StorageManager;
import io.kowalski.claptrap.exception.ClaptrapException;

public class Claptrap extends Application<ClaptrapConfiguration> {

    private final StorageManager storageManager;
    private final GuiceModule guiceModule;

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
        storageManager.startSMTPServer(configuration.getSmtpPort());
    }

}
