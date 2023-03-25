package com.giraone.webflux.security.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpServerErrorException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BalanceServiceIT {

    @Autowired
    BalanceService balanceService;

    @Test
    void getBalanceOfAccount_found() {

        Mono<Integer> result = balanceService.getBalanceOfAccount("A1");
        StepVerifier.create(result)
            .assertNext(balance -> assertThat(balance).isEqualTo(100))
            .verifyComplete();
    }

    @Test
    void getBalanceOfAccount_notFound() {

        Mono<Integer> result = balanceService.getBalanceOfAccount("XXX");
        StepVerifier.create(result)
            .expectError(HttpServerErrorException.class)
            .verify();
    }
}