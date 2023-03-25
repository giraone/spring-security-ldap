package com.giraone.webmvc.security.controller;

import com.giraone.webmvc.security.service.LdapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LdapController {

    private final static Logger LOGGER = LoggerFactory.getLogger(LdapController.class);

    private final LdapService ldapService;

    public LdapController(LdapService ldapService) {
        this.ldapService = ldapService;
    }

    @GetMapping("/ldap/users")
    public List<String> listUsers() {

        LOGGER.info("listUsers");
        return ldapService.listUsers();
    }

    @GetMapping("/ldap/groups")
    public List<String> listGroups() {

        LOGGER.info("listGroups");
        return ldapService.listGroups();
    }
}