package com.javarush.jira;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile("!" + ProfileNames.TEST_POSTGRESQL)
@Configuration
public class H2JPATestConfig {

    @Bean
    public DataSource getDataSource() {
        System.out.println("--- !!! Test with H2 database!!! ---");

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url("jdbc:h2:mem:test");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("");

        return dataSourceBuilder.build();
    }
}