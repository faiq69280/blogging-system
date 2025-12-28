package com.example.article_api.caching;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Qualifier("postCachingAspect")
public class PostCachingAspect extends CachingAspect {

    @Autowired
    PostCachingService postCachingService;

    /**
     * @param key
     * @param object
     * @return
     */
    @Override
    protected Object processObjectAndStoreInCache(String key, Object object) {
        return postCachingService.putInCache(key, object);
    }

    /**
     * @param key
     * @return
     */
    @Override
    protected Object fetchFromCache(String key) {
        return postCachingService.get(key);
    }
}
