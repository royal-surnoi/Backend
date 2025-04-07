package fusionIQ.AI.V2.fusionIq.service;

import com.zaxxer.hikari.HikariConfig;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://fusioniq-dev.cox0wuaim3oy.us-east-1.rds.amazonaws.com:3306/dev?createDatabaseIfNotExist=true");
        config.setUsername("admin");
        config.setPassword("VKBU!LdY0a9phWF.mU~o5kHh5h_*");
        config.addDataSourceProperty("connectionInitSql", "SET GLOBAL max_allowed_packet=134217728"); // 128 MB
        return new HikariDataSource(config);
    }
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}

