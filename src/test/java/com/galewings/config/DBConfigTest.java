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

    @Test
    public void customRssDialectTest() {
        Assertions.assertNotNull(dbConfig.customRssDialect());
    }

    @Test
    public void customRssDataSourceTest() {
        Assertions.assertNotNull(dbConfig.customRssDataSource());
    }

    @Test
    public void customRssConnectionProviderTest() {
        Assertions.assertNotNull(dbConfig.customRssConnectionProvider());
    }

    @Test
    public void customRssTransactionManagerTest() {
        Assertions.assertNotNull(dbConfig.customRssTransactionManager());
    }

    @Test
    public void customRssSqlManagerTest() {
        Assertions.assertNotNull(dbConfig.customRssSqlManager());
    }

}
