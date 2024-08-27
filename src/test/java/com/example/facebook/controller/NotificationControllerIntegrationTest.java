package com.example.facebook.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class NotificationControllerIntegrationTest {


    @Container
    @ServiceConnection
    static MySQLContainer<?> mysqlContainer=
            new MySQLContainer<>(DockerImageName.parse("mysql:latest"));
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getNotifications() throws Exception {
    }

    @Test
    void markAllSeen() {
    }

    @Test
    void markAllRead() {
    }
}