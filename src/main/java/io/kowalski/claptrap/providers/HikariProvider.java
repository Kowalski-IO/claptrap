package io.kowalski.claptrap.providers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.inject.Provider;

public class HikariProvider implements Provider<HikariDataSource> {

    @Override
    public HikariDataSource get() {
        final HikariConfig config = new HikariConfig();
        config.setPoolName("H2_EMBEDDED");
        config.setJdbcUrl("jdbc:h2:tcp://localhost:32769/default");

        return new HikariDataSource(config);
    }

}
