package ru.nonsense.auth.invocationhandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CacheService<T, K, V> {

    protected final Map<T, CacheEntry<K, V>> cache = new ConcurrentHashMap<>();

    public abstract void cachePut(T cacheName, K key, Object object);

    abstract void clearCache();

    public abstract Object cacheGet(T cacheName, K key);

    public abstract boolean cacheContains(T cacheName, K key);
}
