       package com.helpfinder.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

//configuration class where beans are exposed to the entire project
@Configuration 
public class Config {    

    @Value("${db.driverClassName}") String dbDriverClass;
    @Value("${db.url}") String dbUrl;
     @Bean
     public DataSource dataSource() {
         final DriverManagerDataSource dataSource = new DriverManagerDataSource();
         dataSource.setDriverClassName(dbDriverClass);
         dataSource.setUrl(dbUrl);       
         return dataSource;
     }
}
