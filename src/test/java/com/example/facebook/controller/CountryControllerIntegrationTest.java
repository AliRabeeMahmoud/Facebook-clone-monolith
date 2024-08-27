package com.example.facebook.controller;


import com.example.facebook.entity.Country;
import com.example.facebook.repository.CountryRepository;
import com.example.facebook.service.CountryService;
import com.example.facebook.shared.WithMockAuthUser;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CountryControllerIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysqlContainer=
            new MySQLContainer<>(DockerImageName.parse("mysql:latest"));
    @Autowired
    MockMvc mockMvc;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    CountryService countryService;

    private final String API_URL_PREFIX = "/api/v1";

    @BeforeEach
    void setUp() {
        countryRepository.save(Country.builder().name("Bangladesh").build());
    }

    @AfterEach
    void tearDown() {
        countryRepository.deleteAll();
    }

    @Test
    @WithMockAuthUser
    void getCountryList() throws Exception {
        mockMvc.perform(get(API_URL_PREFIX + "/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}