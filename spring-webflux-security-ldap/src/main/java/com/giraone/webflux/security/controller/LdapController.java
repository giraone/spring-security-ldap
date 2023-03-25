package com.giraone.webflux.security.controller;

import com.giraone.webflux.security.service.LdapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class LdapController {

    private final static Logger LOGGER = LoggerFactory.getLogger(LdapController.class);

    private final LdapService ldapService;

    public LdapController(LdapService ldapService) {
        this.ldapService = ldapService;
    }

    @GetMapping("/ldap/users")
    public Mono<List<String>> listUsers() {

        LOGGER.info("listUsers");
        return Mono.just(ldapService.listUsers());
    }

    @GetMapping("/ldap/groups")
    public Mono<List<String>> listGroups() {

        LOGGER.info("listGroups");
        return Mono.just(ldapService.listGroups());
    }
}