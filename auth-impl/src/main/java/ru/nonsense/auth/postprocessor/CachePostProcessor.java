package ru.nonsense.auth.postprocessor;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import ru.nonsense.auth.annotation.MyCacheable;
import ru.nonsense.auth.invocationhandler.CacheKey;
import ru.nonsense.auth.invocationhandler.CacheService;
import ru.nonsense.auth.invocationhandler.CacheValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@Component
@AllArgsConstructor
public class CachePostProcessor implements BeanPostProcessor {

    private final CacheService<String, CacheKey, CacheValue> cacheService;
    private final ExpressionParser parser = new SpelExpressionParser(
            new SpelParserConfiguration(true, true)
    );

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
        if (Stream.concat(interfacesMethods, classMethods).anyMatch(isCacheableMethod())) {
            return createCacheableProxy(type, bean);
        }
        return bean;
    }

    @NotNull
    private Stream<Method> getInterfacesMethods(Class type) {
        return Arrays.stream(type.getInterfaces()).map(Class::getMethods).flatMap(Arrays::stream);
    }

    @NotNull
    private Predicate<Method> isCacheableMethod() {
        return m -> AnnotationUtils.getAnnotation(m, MyCacheable.class) != null;
    }

    @NotNull
    private Object createCacheableProxy(Class type, Object bean) {
        return Proxy.newProxyInstance(type.getClassLoader(), type.getInterfaces(),
                (Object proxy, Method method, Object[] args) -> {
                    Optional<MyCacheable> annotation;
                    annotation = ofNullable(getOverriddenMethod(method, type).getDeclaredAnnotation(MyCacheable.class));

                    if (annotation.isEmpty()) {
                        annotation = getInterfacesMethods(proxy.getClass())
                                .filter(getMethodNamePredicate(method))
                                .filter(isCacheableMethod())
                                .findAny()
                                .map(m -> AnnotationUtils.getAnnotation(m, MyCacheable.class));
                    }

                    if (annotation.isPresent()) {
                        return invokeWithCache(bean, method, args, annotation.get());
                    }

                    return method.invoke(bean, args);
                });
    }

    @NotNull
    private Predicate<Method> getMethodNamePredicate(Method method) {
        return m -> m.getName().equals(method.getName());
    }

    private Method getOverriddenMethod(Method method, Class<?> targetClass) throws NoSuchMethodException {
        return targetClass.getMethod(method.getName(), method.getParameterTypes());
    }

    private Object invokeWithCache(Object bean, Method method, Object[] args, MyCacheable annotation) throws IllegalAccessException, InvocationTargetException {
        CacheKey cacheKey = annotation.key().isBlank()
                ? new CacheKey(args)
                : new CacheKey(getKeyBySpel(method, args, annotation));

        if (cacheService.cacheContains(annotation.name(), cacheKey)) {
            return cacheService.cacheGet(annotation.name(), cacheKey);
        }

        var obj = method.invoke(bean, args);
        cacheService.cachePut(annotation.name(), cacheKey, obj);
        return obj;
    }

    @Nullable
    private Object getKeyBySpel(Method method, Object[] args, MyCacheable annotation) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        Expression spel = parser.parseExpression(annotation.key());

        for (int i = 0; i < args.length; i++) {
            context.setVariable(method.getParameters()[i].getName(), args[i]);
        }

        return spel.getValue(context);
    }
}
