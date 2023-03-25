package com.giraone.webmvc.security.controller;

import com.giraone.webmvc.security.service.BalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    public Map<String, Integer> getBalance(@PathVariable String account) {

        LOGGER.info("getBalance {}", account);
        return Map.of("balance", balanceService.getBalanceOfAccount(account));
    }

    @GetMapping("/api/who-am-i")
    public Map<String, String> whoAmI(Principal principal) {
        return Map.of("name", principal.getName());
    }

    @GetMapping("/api/my-details")
    public Map<String, Object> whoAmIWithDetails(Authentication authentication) {
        // final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return Map.of(
                "authenticated", authentication.isAuthenticated(),
                "name", authentication.getName(),
                "authorities", authentication.getAuthorities()
            );
        } else {
            return Map.of("authenticated", false);
        }
    }
}