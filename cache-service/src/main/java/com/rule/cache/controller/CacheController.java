package com.rule.cache.controller;

import com.rule.cache.service.CacheService;
import com.rule.cache.utils.EhcacheUtils;
import com.rule.cache.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {


    @Autowired
    private CacheService cacheService;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping(value = "/cache")
    public Object get(@RequestParam("cacheName") String cacheName, @RequestParam("key") String key) {
        return cacheService.getCache(cacheName, key);
    }

    @GetMapping(value = "/ehcache/cache")
    public Object getByEhcache(@RequestParam("cacheName") String cacheName, @RequestParam("key") String key) {
        return EhcacheUtils.get(cacheName, key);
    }

    @GetMapping(value = "/redis/cache")
    public Object getByRedis(@RequestParam("cacheName") String cacheName, @RequestParam("key") String key) {
        return redisUtil.get(cacheName+":"+key);
    }

    @PostMapping(value = "/cache")
    public Object put(@RequestParam("cacheName") String cacheName, @RequestParam("key") String key) {
        return cacheService.putCache(cacheName, key);
    }

}
