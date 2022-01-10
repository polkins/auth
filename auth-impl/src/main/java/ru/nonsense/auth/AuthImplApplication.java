package ru.nonsense.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AuthImplApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthImplApplication.class, args);
    }
}
