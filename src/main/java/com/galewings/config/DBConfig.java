package com.galewings.config;

import com.miragesql.miragesql.SqlManagerImpl;
import com.miragesql.miragesql.dialect.SQLiteDialect;
import com.miragesql.miragesql.integration.spring.SpringConnectionProvider;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * DB設定
 */
@SuppressWarnings("unused")
@Configuration
public class DBConfig {

    /**
     * デフォルトDB設定
     *
     * @return DB設定
     */
    @Bean(destroyMethod = "close")
    @Primary
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:library.db");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }

    /**
     * トランザクション管理設定
     *
     * @return トランザクション管理
     */
    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }

    /**
     * SpringConnectionProvider
     *
     * @return SpringConnectionProvider
     */
    @Bean
    @Primary
    public SpringConnectionProvider connectionProvider() {
        SpringConnectionProvider springConnectionProvider = new SpringConnectionProvider();
        springConnectionProvider.setTransactionManager(transactionManager());
        return springConnectionProvider;
    }

    /**
     * SQLitedialect
     *
     * @return SQLitedialect
     */
    @Bean
    @Primary
    public SQLiteDialect dialect() {
        return new SQLiteDialect();
    }

    /**
     * SqlManager
     *
     * @return SqlManager
     */
    @Bean
    @Primary
    public SqlManagerImpl sqlManager() {
        SqlManagerImpl sqlManager = new SqlManagerImpl();
        sqlManager.setConnectionProvider(connectionProvider());
        sqlManager.setDialect(dialect());
        return sqlManager;
    }

    @Bean
    @Primary
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean(name = "customRssDialect")
    public SQLiteDialect customRssDialect() {
        return new SQLiteDialect();
    }

    @Bean(name = "customRssDataSource")
    public DataSource customRssDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:customRss.db");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean(name = "customRssConnectionProvider")
    public SpringConnectionProvider customRssConnectionProvider() {
        SpringConnectionProvider springConnectionProvider = new SpringConnectionProvider();
        springConnectionProvider.setTransactionManager(customRssTransactionManager());
        return springConnectionProvider;
    }

    @Bean(name = "customRssTransactionManager")
    public DataSourceTransactionManager customRssTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(customRssDataSource());
        return dataSourceTransactionManager;
    }

    @Bean(name = "customRssSqlManager")
    public SqlManagerImpl customRssSqlManager() {
        SqlManagerImpl sqlManager = new SqlManagerImpl();
        sqlManager.setConnectionProvider(customRssConnectionProvider());
        sqlManager.setDialect(customRssDialect());
        return sqlManager;
    }

}
