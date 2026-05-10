package com.project.debatepartner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.project.debatepartner.repository")
@EntityScan(basePackages = "com.project.debatepartner.model")
public class DebatepartnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DebatepartnerApplication.class, args);
    }
}