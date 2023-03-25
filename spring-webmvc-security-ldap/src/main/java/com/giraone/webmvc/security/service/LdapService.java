package com.giraone.webmvc.security.service;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LdapService {

    private LdapTemplate ldapTemplate;

    public LdapService(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public List<String> listUsers() {

        // See also: https://docs.spring.io/spring-ldap/docs/3.0.1/reference/
        return ldapTemplate
            .search(
                "ou=users",
                "objectclass=inetOrgPerson",
                (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());
    }

    public List<String> listGroups() {

        // See also: https://docs.spring.io/spring-ldap/docs/3.0.1/reference/
        return ldapTemplate
            .search(
                "ou=groups",
                "objectclass=groupOfNames",
                (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());
    }
}
