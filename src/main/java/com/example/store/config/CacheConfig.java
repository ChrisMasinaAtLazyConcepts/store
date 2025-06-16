package com.example.store.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.store.security.JwtUserDetailsService;

import java.util.List;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Cache manager bean to improve performance by reducing database queries for customer data.
     */
    @Bean
    public ConcurrentMapCacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(List.of("customers"));
        return cacheManager;
    }
}
