package com.giraone.webflux.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.BiFunction;

@Service
public class AccessDecisionConfig {

    private final static Map<String, String> ACCESS = Map.of(
        "user1", "A1",
        "user2", "A1",
        "user3", "A2"
    );

    @Bean
    public BiFunction<String, String, Mono<Boolean>> accessDecision() {
        return (username, account) -> Mono.defer(() -> Mono.just(hasAccess(username, account)));
    }

    private boolean hasAccess(String username, String account) {
        return "admin".equals(username) || account.equals(ACCESS.get(username));
    }
}
