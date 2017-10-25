package io.kowalski.claptrap.providers;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.inject.Inject;
import javax.inject.Provider;

public class DSLContextProvider implements Provider<DSLContext> {

    @Inject
    private HikariDataSource dataSource;

    @Override
    public DSLContext get() {
        return DSL.using(dataSource, SQLDialect.H2);
    }
}
