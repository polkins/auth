package ru.nonsense.auth.invocationhandler;

import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Data
public class CacheInvocationHandler implements InvocationHandler {
    private final CacheService cacheService;
    private final Object original;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return method.invoke(original, args);
    }
}
