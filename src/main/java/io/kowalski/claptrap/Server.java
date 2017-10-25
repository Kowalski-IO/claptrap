package io.kowalski.claptrap;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.kowalski.claptrap.configuration.ServerModule;
import io.kowalski.claptrap.configuration.ServerConfiguration;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class Server extends Application<ServerConfiguration> {

    public static void main(final String[] args) {
        try {
            new Server().run(args);
        } catch (final Exception e) {
            System.err.println("Unable to start Claptrap Server.");
        }
    }

    @Override
    public final void initialize(final Bootstrap<ServerConfiguration> bootstrap) {
        bootstrap.addBundle(GuiceBundle.builder()
                .modules(new ServerModule())
                .enableAutoConfig("io.kowalski.claptrap.resources")
                .build());

        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
    }

    @Override
    public void run(ServerConfiguration configuration, Environment environment) throws Exception {

    }

}
