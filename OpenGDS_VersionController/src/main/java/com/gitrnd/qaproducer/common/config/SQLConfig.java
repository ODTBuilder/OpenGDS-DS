package com.gitrnd.qaproducer.common.config;

import java.sql.SQLException;

import javax.servlet.Filter;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan(basePackages = { "com.gitrnd.qaproducer" })
@PropertySources({ @PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = true),
		@PropertySource(value = "file:./application.yml", ignoreResourceNotFound = true) })
public class SQLConfig extends HikariConfig {

	@Autowired
	private ApplicationContext applicationContext;
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String userName;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${mybatis.config-location}")
	private String config;
	@Value("${mybatis.mapper-location}")
	private String mapper;
	@Value("${spring.datasource.hikari.pool-name}")
	private String pool;
	@Value("${spring.datasource.hikari.minimum-idle}")
	private int minPool;
	@Value("${spring.datasource.hikari.maximum-pool-size}")
	private int maxPool;
	@Value("${spring.datasource.hikari.auto-commit}")
	private Boolean autoCommit;

	@Bean
	public Filter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}

	@Bean(destroyMethod = "close")
	public DataSource dataSource() throws SQLException {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(driverClassName);
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(userName);
		hikariConfig.setPassword(password);
		hikariConfig.setPoolName(pool);
		hikariConfig.setAutoCommit(autoCommit);
		hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
		hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
		hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
		hikariConfig.setMinimumIdle(minPool);
		hikariConfig.setMaximumPoolSize(maxPool);
		hikariConfig.addDataSourceProperty("maxLifetime", "90000");
		hikariConfig.addDataSourceProperty("idleTimeout", "30000");
		hikariConfig.setConnectionInitSql("SELECT 1");

		HikariDataSource dataSource = new HikariDataSource(hikariConfig);
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager() throws SQLException {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
		transactionManager.setGlobalRollbackOnParticipationFailure(false);
		return transactionManager;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		sessionFactoryBean.setTypeAliasesPackage("com.gitrnd.qaproducer");
		sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:config/mybatis.xml"));
		sessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:sql/*.xml"));
		return sessionFactoryBean.getObject();
	}
}
