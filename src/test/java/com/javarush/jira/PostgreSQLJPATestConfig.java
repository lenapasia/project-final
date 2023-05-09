package com.javarush.jira;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile(ProfileNames.TEST_POSTGRESQL)
@Configuration
public class PostgreSQLJPATestConfig {

    @Autowired
    DataSourceProperties dataSourceProperties;

    @Bean
    public DataSource postgresqlDataSource() {
        System.out.println("--- !!! Test with PostgreSQL database!!! ---");

        return dataSourceProperties
                .initializeDataSourceBuilder()
                .build();
    }
}