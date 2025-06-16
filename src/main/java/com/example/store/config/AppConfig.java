package com.example.store.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;


@Configuration
@EnableAsync
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.example.store")
public class AppConfig {
   
}
