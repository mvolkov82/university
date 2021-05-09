package com.foxminded.university.repository;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public class TestValuesInitializer {

    public void initialize(DataSource dataSource) {
        ResourceDatabasePopulator initializer = new ResourceDatabasePopulator();
        initializer.addScript(new ClassPathResource("/sql/create_test_data.sql"));
        DatabasePopulatorUtils.execute(initializer, dataSource);
    }
}
