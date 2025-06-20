package fusionIQ.AI.V2.fusionIq.service;

import com.zaxxer.hikari.HikariConfig;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;


// Devops-testing purpose code has been added
@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = properties
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
        dataSource.addDataSourceProperty("connectionInitSql", "SET GLOBAL max_allowed_packet=134217728");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}

// @Configuration
// public class DataSourceConfig {

//     @Bean
//     public DataSource dataSource() {
//         HikariConfig config = new HikariConfig();
//         config.setJdbcUrl("jdbc:mysql://dev.db.fusioniq.com:3306/dev?createDatabaseIfNotExist=true");
//         config.setUsername("admin");
//         config.setPassword("l8YsXbkYze*7)Qr>||rZ#IWo8$7Y");
//         config.addDataSourceProperty("connectionInitSql", "SET GLOBAL max_allowed_packet=134217728"); // 128 MB
//         return new HikariDataSource(config);
//     }
//     @Bean
//     public PlatformTransactionManager transactionManager() {
//         return new DataSourceTransactionManager(dataSource());
//     }
// }

