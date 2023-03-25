package com.giraone.webmvc.security.config;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AccessDecisionConfig {

    private final static Map<String, String> ACCESS = Map.of(
        "user1", "A1",
        "user2", "A1",
        "user3", "A2"
    );

    public boolean hasAccess(String username, String account) {
        return "admin".equals(username) || account.equals(ACCESS.get(username));
    }
}
