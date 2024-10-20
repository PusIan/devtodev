package org.devtodev.consumer.util.impl;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public class RedisTestContainer {
    @Container
    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

    @BeforeAll
    static void beforeAll() {
        REDIS_CONTAINER.start();
    }

    @AfterAll
    static void afterAll() {
        REDIS_CONTAINER.stop();
    }
}
