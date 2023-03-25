package com.giraone.webflux.security.controller;

import com.giraone.webflux.security.service.BalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;

@RestController
public class DemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

    private final BalanceService balanceService;

    public DemoController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/api/balance/{account}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // static role check, not enough!
    @PreAuthorize("hasAnyRole('ADMIN', #account)") // works, but has no indirection
    // @PreAuthorize("@accessDecision.apply(principal.username, #account)") // works, with indirection
    public Mono<Map<String, Integer>> getBalance(@PathVariable String account) {

        LOGGER.info("getBalance {}", account);
        return balanceService.getBalanceOfAccount(account)
            .map(balance -> Map.of("balance", balance));
    }

    @GetMapping("/api/who-am-i")
    public Mono<Map<String, String>> whoAmI(Mono<Principal> principal) {
        return principal.map(p -> Map.of("name", p.getName()));
    }

    @GetMapping("/api/my-details2")
    public Mono<Map<String, Object>> whoAmIWithDetails2(Mono<Authentication> authentication) {
        return authentication.map(auth -> Map.of(
                    "authenticated", auth.isAuthenticated(),
                    "name", auth.getName(),
                    "authorities", auth.getAuthorities()
                )
            )
            .onErrorReturn(Map.of("authenticated", false));
    }

    @GetMapping("/api/my-details")
    public Mono<Map<String, Object>> whoAmIWithDetails() {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
            .map((authentication) -> Map.of(
                    "authenticated", authentication.isAuthenticated(),
                    "name", authentication.getName(),
                    "authorities", authentication.getAuthorities()
                )
            )
            .onErrorReturn(Map.of("authenticated", false));
    }
}