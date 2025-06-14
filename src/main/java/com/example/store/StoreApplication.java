package com.example.store;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@EnableCaching
public class StoreApplication {

    private final DataSource dataSource = null;
    ;

    @PostConstruct
    public void loadData() {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            ClassPathResource resource = new ClassPathResource("data.sql");
            String sql = new String(Files.readAllBytes(Paths.get(resource.getURI())));
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }
}
