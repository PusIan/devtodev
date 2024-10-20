package org.devtodev.consumer.util.impl;

import lombok.RequiredArgsConstructor;
import org.devtodev.consumer.util.UniqueIdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(classes = {UniqueIdGeneratorImpl.class, RedisAutoConfiguration.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UniqueIdGeneratorImplTest extends RedisTestContainer {
    private final UniqueIdGenerator uniqueIdGenerator;

    @Test
    void generateUniqueId_ConcurrentThreads_AllIdsAreUnique() throws ExecutionException, InterruptedException {
        int numberOfProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfProcessors * 2);
        List<UUID> ids = new ArrayList<>();
        List<Future<List<UUID>>> futures = new ArrayList<>();
        for (int i = 0; i < numberOfProcessors * 2; i++) {
            futures.add(executorService.submit(() -> {
                List<UUID> list = new ArrayList<>();
                int numberOfIdGeneration = 1000;
                for (int j = 0; j < numberOfIdGeneration; j++) {
                    list.add(uniqueIdGenerator.generateUniqueId());
                }
                return list;
            }));
        }
        for (Future<List<UUID>> future : futures) {
            ids.addAll(future.get());
        }
        assertEquals(ids.size(), ids.stream().distinct().count());
    }
}
