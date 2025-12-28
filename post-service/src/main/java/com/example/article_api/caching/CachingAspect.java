package com.example.article_api.caching;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.ConcurrentHashMap;

@Aspect
public abstract class CachingAspect {
    ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<>();

    /**
     *
     * @param proceedingJoinPoint
     * @param annotation
     * @return
     * @throws Throwable
     */
    @Around("@annotation(annotation)")
    public Object fetchData(ProceedingJoinPoint proceedingJoinPoint, LookupMemory annotation) throws Throwable {
        String cacheKey = getKeyFromSignature(proceedingJoinPoint, annotation.key());

        if (cacheKey == null) {
            throw new IllegalArgumentException("Error processing cacheKey from method signature with parameter[%s]".formatted(annotation.key()));
        }

        Object objectFetched = fetchFromCache(cacheKey);
        if (objectFetched != null) {
            return objectFetched;
        }

        //we don't want all concurrent requests to hit the DB, only the first one should hit the DB and put stuff in cache
        objectFetched = concurrentHashMap.computeIfAbsent(cacheKey, (key) -> {
            Object valueFromDB;
            try {
                valueFromDB = proceedingJoinPoint.proceed();
                valueFromDB = processObjectAndStoreInCache(key, valueFromDB);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            return valueFromDB;
        });

        //remove stale objects for concurrent requests
        concurrentHashMap.remove(cacheKey);
        return objectFetched;
    }

    /**
     *
     * @param joinPoint
     * @param keyName
     * @return
     */
    private String getKeyFromSignature(ProceedingJoinPoint joinPoint, String keyName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameters = signature.getParameterNames();
        Object[] arguments = joinPoint.getArgs();

        if (parameters.length != arguments.length) {
            throw new IllegalArgumentException("Parameters and arguments length mismatch in CachingAspect::getKeyFromSignature");
        }

        for (int argumentIndex = 0; argumentIndex < arguments.length; argumentIndex++) {
            if (parameters[argumentIndex].equals(keyName)) {
                return (String) arguments[argumentIndex];
            }
        }
        return null;
    }

    protected abstract Object processObjectAndStoreInCache(String key, Object object);
    protected abstract Object fetchFromCache(String key);
}
