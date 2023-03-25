package com.giraone.webflux.security.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
class DemoControllerIT {

    ParameterizedTypeReference<Map<String, Object>> MAP = new ParameterizedTypeReference<>() {
    };

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenNoCredentials_getBalance_isUnauthorized() {
        webTestClient.get()
            .uri("/api/balance/A1")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    @WithMockUser(value = "other", roles = {"OTHER"})
    void whenHasCredentials_andWrongRole_isForbidden() {
        webTestClient.get()
            .uri("/api/balance/A1")
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(value = "user3", roles = {"USER", "A2"})
    void whenHasCredentials_andWrongUser_isForbidden() {
        webTestClient.get()
            .uri("/api/balance/A1")
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(value = "user1", roles = {"USER", "A1"})
    void whenHasCredentials_andCorrectUser1_isOk() {
        webTestClient.get()
            .uri("/api/balance/A1")
            .exchange()
            .expectStatus().isOk()
            .expectBody(MAP)
            .value(m -> assertThat(m).containsEntry("balance", 100));
    }

    @Test
    @WithMockUser(value = "user2", roles = {"USER", "A1"})
    void whenHasCredentials_andCorrectUser2_isOk() {
        webTestClient.get()
            .uri("/api/balance/A1")
            .exchange()
            .expectStatus().isOk()
            .expectBody(MAP)
            .value(m -> assertThat(m).containsEntry("balance", 100));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"ADMIN"})
    void whenHasCredentials_andAdmin_allEndpointsAreOk() {
        webTestClient.get()
            .uri("/api/balance/A1")
            .exchange()
            .expectStatus().isOk()
            .expectBody(MAP)
            .value(m -> assertThat(m).containsEntry("balance", 100));
        webTestClient.get()
            .uri("/api/balance/A2")
            .exchange()
            .expectStatus().isOk()
            .expectBody(MAP)
            .value(m -> assertThat(m).containsEntry("balance", 200));
    }
}