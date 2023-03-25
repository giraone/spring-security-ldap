package com.giraone.webflux.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebFluxSecurityConfig {

    /*
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {

        UserDetails admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("admin")
            .roles("ADMIN")
            .build();
        UserDetails user1 = User.withDefaultPasswordEncoder()
            .username("user1")
            .password("user1")
            .roles("USER", "A1")
            .build();
        UserDetails user2 = User.withDefaultPasswordEncoder()
            .username("user2")
            .password("user2")
            .roles("USER", "A2")
            .build();
        UserDetails user3 = User.withDefaultPasswordEncoder()
            .username("user3")
            .password("user3")
            .roles("USER", "A3")
            .build();
        UserDetails other = User.withDefaultPasswordEncoder()
            .username("other")
            .password("other")
            .roles("OTHER")
            .build();
        return new MapReactiveUserDetailsService(admin, user1, user2, user3, other);
    }
     */

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange((authorize) -> authorize
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/ldap/**").permitAll() // remove in PRODUCTION
                .pathMatchers("index.html").permitAll()
                .pathMatchers("/api/**").authenticated()
                .anyExchange().denyAll()
            )
            .httpBasic(withDefaults());
        return http.build();
    }
}
