package com.giraone.webflux.security.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
class LdapControllerIT {

    ParameterizedTypeReference<List<String>> LIST = new ParameterizedTypeReference<>() {
    };

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void listUsers() {
        List<String> users = webTestClient.get()
            .uri("/ldap/users")
            .exchange()
            .expectStatus().isOk()
            .expectBody(LIST)
            .returnResult()
            .getResponseBody();
        assertThat(users).containsExactlyInAnyOrder("admin", "user1", "user2", "user3", "other");
    }

    @Test
    void listGroups() {
        List<String> groups = webTestClient.get()
            .uri("/ldap/groups")
            .exchange()
            .expectStatus().isOk()
            .expectBody(LIST)
            .returnResult()
            .getResponseBody();
        assertThat(groups).containsExactlyInAnyOrder("ADMIN", "USER", "A1", "A2");
    }
}