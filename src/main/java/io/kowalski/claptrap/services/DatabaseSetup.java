package io.kowalski.claptrap.services;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class DatabaseSetup {

    @Inject
    public DatabaseSetup(DSLContext dslContext) {
        DatabaseSetup.buildDatabase(dslContext);
    }

    private static void buildDatabase(DSLContext dsl) {
        try {
            StringBuilder query = new StringBuilder();
            Path path = Paths.get(DatabaseSetup.class.getClassLoader()
                    .getResource("sql/schema.sql").toURI());
            Files.lines(path).forEach(l -> query.append(l).append(" "));
            dsl.execute(query.toString());
        } catch (URISyntaxException | IOException e) {
            log.error("Unable to generate database. Aborting...", e);
            throw new IllegalStateException(e);
        }
    }

}
