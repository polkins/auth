package ru.nonsense.auth.common;

import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import ru.nonsense.auth.config.WebSecurityConfig;
import ru.nonsense.auth.invocationhandler.CacheServiceImpl;

@Profile("test")
@Configuration
@Import(WebSecurityConfig.class)
@ComponentScan(value = {"ru.nonsense.auth"})
public class TestConfiguration {
    @SpyBean
    public CacheServiceImpl cacheService;
}
