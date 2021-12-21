package ru.nonsense.auth.invocationhandler;

public interface CacheService {

    void cachePut(CacheKey cacheKey, Object value);

    void cachePutEveryInvocation(Object value, CacheKey cacheKey);

    void cacheEvict(CacheKey cacheKey);

    void clearCache();

    Object cacheGet(CacheKey cacheKey);

    boolean cacheContains(CacheKey cacheKey);
}
