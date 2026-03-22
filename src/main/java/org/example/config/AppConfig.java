package org.example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "org.example")
@ComponentScan(basePackages = {
    "com.example.service",
    "com.example.repository",
    "com.example.model.entity"
})
@EnableTransactionManagement
@Import(DatabaseConfig.class)
public class AppConfig {

}
