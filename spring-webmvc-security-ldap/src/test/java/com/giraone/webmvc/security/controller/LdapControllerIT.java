package com.giraone.webmvc.security.controller;

import com.giraone.webmvc.security.config.WebSecurityTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(WebSecurityTestConfig.class)
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