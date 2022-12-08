package com.rule.nio;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.lang.Nullable;

public interface Callback<K, V> {

    @Nullable
    <T> T check(RedisCallback<T> action);
}
