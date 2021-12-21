package ru.nonsense.auth.invocationhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.nonsense.auth.common.AbstractIntegrationTest;
import ru.nonsense.auth.domain.entity.user.AuthUser;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CacheServiceImplTest extends AbstractIntegrationTest {
    @Autowired
    public CacheServiceImpl cacheService;

    @Test
    public void testCacheIsCleanedByScheduler() throws InterruptedException {
        AuthUser authUser = new AuthUser().setFirstName("test").setSurName("test").setId(1L).setLogin("test");
        CacheKey cacheKey = new CacheKey("test");
        cacheService.cachePut(cacheKey, authUser);
        assertThat(cacheService.cacheContains(cacheKey)).isTrue();

        sleep(6000);

        assertThat(cacheService.cacheContains(cacheKey)).isFalse();
    }
}