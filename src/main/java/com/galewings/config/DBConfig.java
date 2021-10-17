package com.galewings.config;

import com.miragesql.miragesql.SqlManagerImpl;
import com.miragesql.miragesql.dialect.SQLiteDialect;
import com.miragesql.miragesql.integration.spring.SpringConnectionProvider;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

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
  public SQLiteDialect dialect() {
    return new SQLiteDialect();
  }

  /**
   * SqlManager
   *
   * @return SqlManager
   */
  @Bean
  public SqlManagerImpl sqlManager() {
    SqlManagerImpl sqlManager = new SqlManagerImpl();
    sqlManager.setConnectionProvider(connectionProvider());
    sqlManager.setDialect(dialect());
    return sqlManager;
  }

}
