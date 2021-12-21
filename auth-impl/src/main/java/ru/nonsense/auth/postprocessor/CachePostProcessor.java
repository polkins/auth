package ru.nonsense.auth.postprocessor;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import ru.nonsense.auth.annotation.MyCacheable;
import ru.nonsense.auth.invocationhandler.CacheKey;
import ru.nonsense.auth.invocationhandler.CacheService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@Component
@AllArgsConstructor
public class CachePostProcessor implements BeanPostProcessor {

    private final CacheService cacheService;

    @Override
    public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class type = bean.getClass();
        return getProxyForCacheable(type, bean);
    }

    @Nullable
    private Object getProxyForCacheable(Class type, Object bean) {
        var interfacesMethods = getInterfacesMethods(type);
        var classMethods = Arrays.stream(type.getMethods());
        if (Stream.concat(interfacesMethods, classMethods).anyMatch(getCacheablePredicate())) {
            return createCacheableProxy(type, bean);
        }
        return bean;
    }

    @NotNull
    private Stream<Method> getInterfacesMethods(Class type) {
        return Arrays.stream(type.getInterfaces()).map(Class::getMethods).flatMap(Arrays::stream);
    }

    @NotNull
    private Predicate<Method> getCacheablePredicate() {
        return m -> AnnotationUtils.getAnnotation(m, MyCacheable.class) != null;
    }

    @NotNull
    private Object createCacheableProxy(Class type, Object bean) {
        return Proxy.newProxyInstance(type.getClassLoader(), type.getInterfaces(),
                (Object proxy, Method method, Object[] args) -> {
                    if (ofNullable(method.getDeclaredAnnotation(MyCacheable.class)).isPresent()
                            || getInterfacesMethods(proxy.getClass()).filter(getMethodNamePredicate(method)).anyMatch(getCacheablePredicate())) {
                        return invokeWithCache(bean, method, args);
                    }
                    return method.invoke(bean, args);
                });
    }

    @NotNull
    private Predicate<Method> getMethodNamePredicate(Method method) {
        return m -> m.getName().equals(method.getName());
    }

    private Object invokeWithCache(Object bean, Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
        CacheKey cacheKey = new CacheKey(args);

        if (cacheService.cacheContains(cacheKey)) {
            return cacheService.cacheGet(cacheKey);
        }

        var obj = method.invoke(bean, args);
        cacheService.cachePut(cacheKey, obj);
        return obj;
    }
}
