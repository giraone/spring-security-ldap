package com.giraone.webmvc.security.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Map;

@Service
public class BalanceService {

    private static final Map<String, Integer> BALANCES = Map.of(
        "A1", 100,
        "A2", 200,
        "A3", 300
    );

    public int getBalanceOfAccount(String account) {

        Integer ret = BALANCES.get(account);
        if (ret == null) {
            throw new HttpServerErrorException(HttpStatus.NOT_FOUND);
        }
        return ret;
    }
}
