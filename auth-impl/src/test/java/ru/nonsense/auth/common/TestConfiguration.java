package ru.nonsense.auth.common;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import ru.nonsense.auth.config.WebSecurityConfig;

@Profile("test")
@Configuration
@Import(WebSecurityConfig.class)
@ComponentScan(value = {"ru.nonsense.auth"})
public class TestConfiguration {
}
