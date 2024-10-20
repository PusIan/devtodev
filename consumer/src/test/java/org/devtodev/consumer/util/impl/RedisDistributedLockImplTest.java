package org.devtodev.consumer.util.impl;

import lombok.RequiredArgsConstructor;
import org.devtodev.consumer.util.DistributedLock;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(classes = {RedisDistributedLockImpl.class, RedisAutoConfiguration.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class RedisDistributedLockImplTest extends RedisTestContainer {
    private final DistributedLock distributedLock;
    private final Random random = new Random();

    @Test
    void acquireLock_Lock_IsSuccessfully() {
        String lockKey = String.valueOf(random.nextLong());
        try {
            assertTrue(distributedLock.acquireLock(lockKey));
        } finally {
            distributedLock.releaseLock(lockKey);
        }
    }

    @Test
    void acquireLock_Reentrant_ReturnFalse() {
        String lockKey = String.valueOf(random.nextLong());
        try {
            distributedLock.acquireLock(lockKey);
            assertFalse(distributedLock.acquireLock(lockKey));
        } finally {
            distributedLock.releaseLock(lockKey);
        }
    }

    @RepeatedTest(100)
    void acquireLock_WhenLockIsAlreadyAcquired_ReturnsFalse() throws InterruptedException {
        String lockKey = String.valueOf(random.nextLong());
        int delayMillisForThread = 10;
        CompletableFuture.runAsync(() -> {
            try {
                distributedLock.acquireLock(lockKey);
                Thread.sleep(delayMillisForThread);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                distributedLock.releaseLock(lockKey);
            }
        });
        Thread.sleep(delayMillisForThread / 2);
        assertFalse(distributedLock.acquireLock(lockKey));
    }

    @Test
    void releaseLock_AfterRelease_LockReturnsTrue() {
        String lockKey = String.valueOf(random.nextLong());
        try {
            distributedLock.acquireLock(lockKey);
            distributedLock.releaseLock(lockKey);
            assertTrue(distributedLock.acquireLock(lockKey));
        } finally {
            distributedLock.releaseLock(lockKey);
        }
    }
}