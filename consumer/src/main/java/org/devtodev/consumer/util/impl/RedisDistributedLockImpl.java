package org.devtodev.consumer.util.impl;

import org.devtodev.consumer.util.DistributedLock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisDistributedLockImpl implements DistributedLock {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisDistributedLockImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean acquireLock(String lockKey, long timeout, TimeUnit unit) {
        return redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", timeout, unit);
    }

    @Override
    public void releaseLock(String lockKey) {
        redisTemplate.delete(lockKey);
    }
}
