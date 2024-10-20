package org.devtodev.consumer.util;

import java.util.concurrent.TimeUnit;

public interface DistributedLock {

    boolean acquireLock(String lockKey, long timeout, TimeUnit unit);

    default boolean acquireLock(String lockKey) {
        return acquireLock(lockKey, 1, TimeUnit.MINUTES);
    }

    void releaseLock(String lockKey);
}
