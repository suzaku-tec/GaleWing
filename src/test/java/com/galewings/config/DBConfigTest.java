package com.galewings.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DBConfigTest {

    DBConfig dbConfig = new DBConfig();

    @Test
    public void dataSourceTest() {
        Assertions.assertNotNull(dbConfig.dataSource());
    }

    @Test
    public void transactionManagerTest() {
        Assertions.assertNotNull(dbConfig.transactionManager());
    }

    @Test
    public void connectionProviderTest() {
        Assertions.assertNotNull(dbConfig.connectionProvider());
    }

    @Test
    public void dialectTest() {
        Assertions.assertNotNull(dbConfig.dialect());
    }

    @Test
    public void sqlManagerTest() {
        Assertions.assertNotNull(dbConfig.sqlManager());
    }

    @Test
    public void jdbcTemplateTest() {
        Assertions.assertNotNull(dbConfig.jdbcTemplate());
    }
}
