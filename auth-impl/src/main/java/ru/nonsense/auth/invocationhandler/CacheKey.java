package ru.nonsense.auth.invocationhandler;

import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

public class CacheKey implements Serializable {
    public static final SimpleKey EMPTY = new SimpleKey(new Object[0]);
    private final Object[] params;
    private transient int hashCode;

    public CacheKey(Object... elements) {
        Assert.notNull(elements, "Elements must not be null");
        this.params = (Object[])elements.clone();
        this.hashCode = Arrays.deepHashCode(this.params);
    }

    public boolean equals(@Nullable Object other) {
        return this == other || other instanceof CacheKey && Arrays.deepEquals(this.params, ((CacheKey)other).params);
    }

    public final int hashCode() {
        return this.hashCode;
    }

    public String toString() {
        return this.getClass().getSimpleName() + " [" + StringUtils.arrayToCommaDelimitedString(this.params) + "]";
    }
}
