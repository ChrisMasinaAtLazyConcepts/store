package com.example.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import com.example.store.security.JwtUserDetailsService;

@Configuration
@EnableAsync
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.example.store")
public class AppConfig {
    @Bean
    public JwtUserDetailsService getJwtUserDetailsService () {
        return new JwtUserDetailsService();
    }
}
