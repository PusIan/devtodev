package org.devtodev.consumer.util.impl;

import org.devtodev.consumer.util.UniqueIdGenerator;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Component
public class UniqueIdGeneratorImpl implements UniqueIdGenerator {
    private final RedisTemplate<String, String> redisTemplate;
    private final ZoneOffset zoneOffset = ZoneOffset.UTC;
    private final Duration ID_KEY_TTL = Duration.ofMinutes(1);
    private final String ID_KEY = "ID";

    public UniqueIdGeneratorImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public UUID generateUniqueId() {
        long firstPart = LocalDateTime.now().toEpochSecond(zoneOffset);
        long secondPart = getSecondPartForUUID();
        return new UUID(firstPart, secondPart);
    }

    private long getSecondPartForUUID() {
        List<Object> txResults = redisTemplate.execute(new SessionCallback<>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForValue().increment(ID_KEY);
                operations.opsForValue().getAndExpire(ID_KEY, ID_KEY_TTL);
                return operations.exec();
            }
        });
        return Long.parseLong(txResults.get(0).toString());
    }
}
