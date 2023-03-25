package com.giraone.webmvc.security.config;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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

        boolean result = accessDecisionConfig.hasAccess(username, account);
        assertThat(result).isEqualTo(expectedResult);
    }
}