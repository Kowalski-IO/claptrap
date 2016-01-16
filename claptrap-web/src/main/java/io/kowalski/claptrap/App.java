package io.kowalski.claptrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.server.SMTPServer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hubspot.dropwizard.guice.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.kowalski.claptrap.configuration.ClaptrapConfiguration;
import io.kowalski.claptrap.configuration.GuiceModule;
import io.kowalski.claptrap.handlers.SMTPMessageHandlerFactory;
import io.kowalski.claptrap.services.impl.EmailParser;

public class App extends Application<ClaptrapConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private final GuiceModule guiceModule;

    public App() {
        guiceModule = new GuiceModule();
    }

    public static void main(final String[] args) {
        try {
            new App().run(args);
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            LOGGER.error("Claptrap hit an unexpected exception. Shutting down.");
            System.exit(-1);
        }
    }

    @Override
    public void initialize(final Bootstrap<ClaptrapConfiguration> bootstrap) {
        bootstrap.addBundle(new Java8Bundle());
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addBundle(GuiceBundle.<ClaptrapConfiguration> newBuilder()
                .addModule(guiceModule)
                .setConfigClass(ClaptrapConfiguration.class)
                .enableAutoConfig(getClass().getPackage().getName())
                .build());
    }

    @Override
    public void run(final ClaptrapConfiguration configuration, final Environment environment) throws Exception, RuntimeException {
        final Injector injector = Guice.createInjector(guiceModule);

        final MessageHandlerFactory smhf = injector.getInstance(SMTPMessageHandlerFactory.class);

        final SMTPServer smtpServer = new SMTPServer(smhf);
        smtpServer.setSoftwareName("Claptrap SMTP");
        smtpServer.setPort(2525);
        smtpServer.start();

        final EmailParser emailParser = injector.getInstance(EmailParser.class);
        new Thread(emailParser, "Claptrap Email Parser").start();

    }
}
