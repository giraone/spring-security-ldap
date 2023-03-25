package com.giraone.webmvc.security.service;

import com.giraone.webmvc.security.config.WebSecurityTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.HttpServerErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(WebSecurityTestConfig.class)
class BalanceServiceIT {

    @Autowired
    BalanceService balanceService;

    @Test
    void getBalanceOfAccount_found() {

        int balance = balanceService.getBalanceOfAccount("A1");
        assertThat(balance).isEqualTo(100);
    }

    @Test
    void getBalanceOfAccount_notFound() {

        assertThatThrownBy(() -> balanceService.getBalanceOfAccount("XXX"))
            .isInstanceOf(HttpServerErrorException.class)
            .hasMessageContaining("");
    }
}