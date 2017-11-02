package io.kowalski.claptrap.configuration;

import com.google.inject.name.Names;
import com.zaxxer.hikari.HikariDataSource;
import io.kowalski.claptrap.providers.DSLContextProvider;
import io.kowalski.claptrap.providers.HikariProvider;
import io.kowalski.claptrap.services.DatabaseSetup;
import io.kowalski.claptrap.smtp.Server;
import org.jooq.DSLContext;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

public class ServerModule extends DropwizardAwareModule<ServerConfiguration> {

    @Override
    protected void configure() {

        bind(Integer.class).annotatedWith(Names.named(Constants.SMTP_SERVER_PORT))
                .toInstance(configuration().getSmtpPort());

        bind(Server.class).asEagerSingleton();
        bind(HikariDataSource.class).toProvider(HikariProvider.class).asEagerSingleton();
        bind(DSLContext.class).toProvider(DSLContextProvider.class);
        bind(DatabaseSetup.class).asEagerSingleton();
    }

}
