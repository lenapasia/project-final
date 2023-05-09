package com.javarush.jira;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.sql.DataSource;

//https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
//https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications-testing-with-mock-environment
public abstract class AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

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
