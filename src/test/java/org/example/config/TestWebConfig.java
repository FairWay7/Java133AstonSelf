package org.example.config;

import org.example.controller.UserController;
import org.example.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.mock;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = UserController.class)
public class TestWebConfig {
    @Bean
    public UserService userService() {
        return mock(UserService.class);
    }
}
