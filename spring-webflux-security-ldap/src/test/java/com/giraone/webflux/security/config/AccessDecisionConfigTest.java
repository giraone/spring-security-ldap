package com.giraone.webflux.security.config;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes = AccessDecisionConfig.class)
class AccessDecisionConfigTest {

    @Autowired
    AccessDecisionConfig accessDecisionConfig;

    @ParameterizedTest
    @CsvSource({
        "user1,A1,true",
        "user1,A2,false",
        "user2,A1,true",
        "user2,A2,false",
        "user3,A1,false",
        "user3,A2,true",
        "admin,A1,true",
        "admin,A2,true"
    })
    void accessDecision(String username, String account, boolean expectedResult) {

        Mono<Boolean> mono = accessDecisionConfig.accessDecision().apply(username, account);
        StepVerifier.create(mono)
            .expectNext(expectedResult)
            .verifyComplete();
    }
}