package com.rule.cache.service;

import com.rule.cache.entities.User;
import com.rule.cache.mapper.UserMapper;
import com.rule.cache.utils.EhcacheUtils;
import com.rule.cache.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CacheService {

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private UserMapper userMapper;

    public Object getCache(String cacheName, String key) {

        // 先从ehcache中获取
        Object cache = EhcacheUtils.get(cacheName, key);
        String redisKey = cacheName + ":" + key;
        if (cache == null) {
            cache = redisUtil.get(redisKey);
            if (cache != null) {
                // 必须设置本地缓存超时时间
                EhcacheUtils.put(cacheName, key, cache);
                return cache;
            }
        }
        if (cache == null) {
            User user = new User();
            user.setId(Integer.parseInt(key));
            cache = userMapper.selectOne(user);
            redisUtil.set(redisKey, user);
            // 必须设置本地缓存超时时间
            EhcacheUtils.put(cacheName, key, cache);
        }
        return cache;
    }

    public Object putCache(String cacheName, String key) {
        // 先从ehcache中删除
        EhcacheUtils.remove(cacheName, key);
        redisUtil.del(cacheName + ":" + key);

        User user = new User();
        user.setId(Integer.parseInt(key));
        User dbUser = userMapper.selectOne(user);
        if (dbUser == null) {
            userMapper.insert(user);
        }
        return user;
    }

}
