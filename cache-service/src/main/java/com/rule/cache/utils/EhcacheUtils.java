package com.rule.cache.utils;

import net.sf.ehcache.CacheManager;
import org.springframework.beans.BeansException;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * EhcacheUtils
 *
 * @version 1.0.0
 * @description ehcache缓存工具类
 */
@Component
public final class EhcacheUtils implements ApplicationContextAware {

    private EhcacheUtils() {
    }

    private static EhCacheCacheManager ehCacheCacheManager = null;

    public static void clear(String cacheName) {
        Cache cache = ehCacheCacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }

    public static Boolean remove(String cacheName, String key) {
        CacheManager cacheManager = ehCacheCacheManager.getCacheManager();
        if (cacheManager != null) {
            net.sf.ehcache.Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                return cache.remove(key);
            }
        }
        return null;
    }

    public static void put(String cacheName, String key, Object value) {
        Cache cache = ehCacheCacheManager.getCache(cacheName);
        if (cache != null) {
            cache.put(key, value);
        }
    }

    public static Object get(String cacheName, String key) {
        Cache cache = ehCacheCacheManager.getCache(cacheName);
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            if (valueWrapper != null) {
                return valueWrapper.get();
            }
        }
        return null;
    }

    public static <T> T get(String cacheName, String key, Class<T> clazz) {
        return clazz.cast(get(cacheName, key));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ehCacheCacheManager = applicationContext.getBean(EhCacheCacheManager.class);
    }
}
