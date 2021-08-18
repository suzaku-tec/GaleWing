package com.example.galewings;

import com.miragesql.miragesql.SqlManagerImpl;
import com.miragesql.miragesql.dialect.SQLiteDialect;
import com.miragesql.miragesql.integration.spring.SpringConnectionProvider;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:library.db");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }

    @Bean
    public SpringConnectionProvider connectionProvider() {
        SpringConnectionProvider springConnectionProvider = new SpringConnectionProvider();
        springConnectionProvider.setTransactionManager(transactionManager());
        return springConnectionProvider;
    }

    @Bean
    public SQLiteDialect dialect() {
        return new SQLiteDialect();
    }

    @Bean
    public SqlManagerImpl sqlManager() {
        SqlManagerImpl sqlManager = new SqlManagerImpl();
        sqlManager.setConnectionProvider(connectionProvider());
        sqlManager.setDialect(dialect());
        return sqlManager;
    }
}
