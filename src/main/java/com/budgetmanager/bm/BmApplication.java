package com.budgetmanager.bm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class BmApplication {
    public static void main(String[] args) {
        SpringApplication.run(BmApplication.class, args);
    }
}
