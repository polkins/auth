package ru.nonsense.auth.invocationhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;
import ru.nonsense.api.dto.UserDto;
import ru.nonsense.auth.common.AbstractIntegrationTest;
import ru.nonsense.auth.domain.entity.user.AuthUser;
import ru.nonsense.auth.mapper.AuthUserDtoMapper;
import ru.nonsense.auth.service.AuthUserService;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;
import static ru.nonsense.auth.ParametrizedTypeReferenceHolder.USERS_REFERENCE;
import static ru.nonsense.auth.utils.AuthUtils.FULL_PATH_CREATE_USERS;

public class CacheServiceImplTest extends AbstractIntegrationTest {
    @Autowired
    public CacheServiceImpl cacheService;
    @Autowired
    private AuthUserService authUserService;
    @Autowired
    private AuthUserDtoMapper authUserDtoMapper;

    @Test
    public void testCacheIsCleanedByScheduler() {
        // given
        AuthUser authUser = new AuthUser().setFirstName("test").setSurName("test").setId(1L).setLogin("test");
        CacheKey cacheKey = new CacheKey("test");
        String cacheName = "testCache";

        // when
        cacheService.cachePut(cacheName, cacheKey, authUser);
        assertThat(cacheService.cacheContains(cacheName, cacheKey)).isTrue();
        await().atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> verify(cacheService, times(2)).clearCache());

        // then
        assertThat(cacheService.cacheContains(cacheName, cacheKey)).isFalse();
    }

    @Test
    public void testCacheKey() {
        //given
        UserDto userDto = new UserDto()
                .setFirstName("Иванов")
                .setLastName("Иван")
                .setSurName("Иванович")
                .setLogin("ivanov")
                .setPassword("1");

        //when
        HttpEntity<?> request2 = new HttpEntity<>(List.of(userDto), getHeaders());
        ResponseEntity<List<AuthUser>> createUser = restTemplate.exchange(
                FULL_PATH_CREATE_USERS,
                HttpMethod.POST,
                request2,
                USERS_REFERENCE
        );
        userDto.setId(createUser.getBody().get(0).getId());
        CacheKey cacheKey = new CacheKey(userDto.getId());

        //then
        authUserService.findUser(userDto);
        assertThat(cacheService.cacheContains("users", cacheKey)).isTrue();
    }
}