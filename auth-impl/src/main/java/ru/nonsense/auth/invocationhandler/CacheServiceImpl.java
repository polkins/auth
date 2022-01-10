package ru.nonsense.auth.invocationhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.util.Optional.ofNullable;

@Component
@Slf4j
public class CacheServiceImpl extends CacheService<String, CacheKey, CacheValue> {

    @Value("${cache.expireAfterWrite}")
    private Long expireAfterWrite;

    @Value("${cache.expireAfterAccess}")
    private Long expireAfterAccess;

    @Override
    public void cachePut(String cacheName, CacheKey key, Object object) {
        if (cache.containsKey(cacheName)) {
            cache.get(cacheName).computeIfAbsent(key, k -> new CacheValue(object, LocalDateTime.now().plusSeconds(expireAfterWrite)));
        } else {
            CacheEntry<CacheKey, CacheValue> cacheEntry = new CacheEntry<>();
            cacheEntry.put(key, new CacheValue(object, LocalDateTime.now().plusSeconds(expireAfterWrite)));
            cache.put(cacheName, cacheEntry);
        }
        log.info("No cache name {} in cache", cacheName);
    }

    @Scheduled(fixedDelayString = "${cache.delay.scheduler}")
    @Override
    void clearCache() {
        LocalDateTime now = LocalDateTime.now();
        cache.values().forEach(ce -> ce.entrySet().removeIf(entry -> entry.getValue().getExpireTime().isBefore(now)));
    }

    @Override
    public Object cacheGet(String cacheName, CacheKey key) {
        var namedCache = this.cache.getOrDefault(cacheName, null);
        if (namedCache != null) {
            var oldValue = namedCache.get(key);
            namedCache.computeIfPresent(key,
                    (k, v) -> new CacheValue(oldValue.getData(), oldValue.getExpireTime().plusSeconds(expireAfterAccess))
            );
            return oldValue.getData();
        }
        return new Object();
    }

    @Override
    public boolean cacheContains(String cacheName, CacheKey key) {
        return ofNullable(cache.getOrDefault(cacheName, null))
                .map(cache -> cache.containsKey(key))
                .orElse(false);
    }
}
