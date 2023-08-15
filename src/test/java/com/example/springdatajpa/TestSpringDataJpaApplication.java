package com.example.springdatajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@TestConfiguration(proxyBeanMethods = false)
public class TestSpringDataJpaApplication {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                .withCopyFileToContainer(MountableFile.forClasspathResource("db/schema.sql"), "/docker-entrypoint-initdb.d/schema.sql")
                .waitingFor(Wait.forListeningPort());
    }

    public static void main(String[] args) {
        SpringApplication.from(SpringDataJpaApplication::main).with(TestSpringDataJpaApplication.class).run(args);
    }
}
