package com.giraone.webmvc.security.service;

import com.giraone.webmvc.security.config.WebSecurityTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(WebSecurityTestConfig.class)
class LdapServiceIT {

    @Autowired
    LdapService ldapService;

    @Test
    void listUsers() {

        List<String> users = ldapService.listUsers();
        assertThat(users).containsExactlyInAnyOrder("admin", "user1", "user2", "user3", "other");
    }

    @Test
    void listGroups() {

        List<String> groups = ldapService.listGroups();
        assertThat(groups).containsExactlyInAnyOrder("ADMIN", "USER", "A1", "A2");
    }
}