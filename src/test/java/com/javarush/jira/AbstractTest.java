package com.javarush.jira;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

@SpringBootTest
@ActiveProfiles("test")
public abstract class AbstractTest {
    @Autowired
    DataSource dataSource;

    @Autowired
    Environment env;

    @BeforeEach
    void populateTestData() {
        final String resetAutoIncrement = isPostgreSQL()
                ? "db/reset-auto-increment-postgresql.sql"
                : "db/reset-auto-increment-h2.sql";

        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(
                new ClassPathResource("db/clear-tables.sql"),
                new ClassPathResource(resetAutoIncrement),
                new ClassPathResource("db/add-test-data.sql")
        );
        populator.setSqlScriptEncoding("UTF-8");

        populator.execute(this.dataSource);
    }

    private boolean isPostgreSQL() {
        return env.acceptsProfiles(Profiles.of(ProfileNames.TEST_POSTGRESQL));
    }
}