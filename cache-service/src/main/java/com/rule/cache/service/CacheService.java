package com.rule.cache.service;

import com.rule.cache.entities.User;
import com.rule.cache.mapper.UserMapper;
import com.rule.cache.utils.EhcacheUtils;
import com.rule.cache.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class CacheService {

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private UserMapper userMapper;

    private static final String nullStr = "null";

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
        // redis 中为空
        if (cache == null) {
            User user = new User();
            user.setId(Integer.parseInt(key));
            cache = userMapper.selectOne(user);
            // 此时cache为数据库中的数据，存在则返回，否则设置null字符串,防止缓存穿透
            // 或者是布隆过滤器
            redisUtil.set(redisKey, Objects.isNull(cache) ? nullStr : cache, 2000);
            // 必须设置本地缓存超时时间
            EhcacheUtils.put(cacheName, key, Objects.isNull(cache) ? nullStr : cache);
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

        // 延时再次 从ehcache redis中删除
        EhcacheUtils.remove(cacheName, key);
        redisUtil.del(cacheName + ":" + key);
        return user;
    }

}
