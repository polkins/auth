package ru.nonsense.auth.invocationhandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheServiceImpl implements CacheService {

    @Value("${cache.ttl}")
    private Long ttl;

    private static final Map<CacheKey, CacheValue> cache = new ConcurrentHashMap<>();

    @Override
    public void cachePut(CacheKey cacheKey, Object value) {
        cache.computeIfAbsent(cacheKey, k -> new CacheValue(value, LocalDateTime.now().plusSeconds(ttl / 1000)));
    }

    @Override
    public void cachePutEveryInvocation(Object value, CacheKey cacheKey) {
        CacheValue cacheValue = new CacheValue(value, LocalDateTime.now().plusSeconds(ttl / 1000));
        cache.put(cacheKey, cacheValue);
    }

    @Override
    public void cacheEvict(CacheKey cacheKey) {
        cache.remove(cacheKey);
    }

    @Scheduled(fixedDelayString = "${cache.ttl}")
    @Override
    public void clearCache() {
        LocalDateTime now = LocalDateTime.now();
        cache.entrySet().removeIf(entry -> entry.getValue().getExpireTime().isBefore(now));
    }

    @Override
    public Object cacheGet(CacheKey cacheKey) {
        return cache.getOrDefault(cacheKey, null).getData();
    }

    @Override
    public boolean cacheContains(CacheKey cacheKey) {
        return cache.containsKey(cacheKey);
    }
}
