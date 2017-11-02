package io.kowalski.claptrap;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.kowalski.claptrap.configuration.ServerModule;
import io.kowalski.claptrap.configuration.ServerConfiguration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import ru.vyarus.dropwizard.guice.GuiceBundle;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

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
    public void run(ServerConfiguration configuration, Environment environment) {
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter("allowedOrigins", "*"); // allowed origins comma separated
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowedMethods", "GET,PUT,POST,DELETE,OPTIONS,HEAD");
        filter.setInitParameter("preflightMaxAge", "5184000"); // 2 months
        filter.setInitParameter("allowCredentials", "true");
    }

}
