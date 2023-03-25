package com.giraone.webmvc.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/ldap/**").permitAll() // remove in PRODUCTION
                .requestMatchers("index.html").permitAll()
                .requestMatchers("/api/**").fullyAuthenticated()
                .anyRequest().denyAll()
            )
            .httpBasic(withDefaults());

        return http.build();
    }
}
