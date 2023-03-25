package com.giraone.webflux.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.server.UnboundIdContainer;

// See https://reflectoring.io/spring-boot-testconfiguration/
@TestConfiguration
public class WebSecurityTestConfig {

    // See https://github.com/spring-projects/spring-security-samples/blob/main/servlet/java-configuration/authentication/username-password/ldap/src/main/java/example/SecurityConfiguration.java

    private final static Logger LOGGER = LoggerFactory.getLogger(WebSecurityTestConfig.class);

    private static final String BASE_PATH = "dc=example,dc=org";

    @Bean
    UnboundIdContainer ldapContainer() {
        UnboundIdContainer result = new UnboundIdContainer(BASE_PATH, "classpath:users-test.ldif");
        result.setPort(0);
        return result;
    }

    @Bean
    ContextSource contextSource(UnboundIdContainer container) {
        final String providerUrl = "ldap://localhost:" + container.getPort() + "/" + BASE_PATH;
        LOGGER.info("WebSecurityTestConfig: Using LdapContextSource with LDAP providerUrl={}", providerUrl);
        final LdapContextSource ret = new DefaultSpringSecurityContextSource(providerUrl);
        LOGGER.info("WebSecurityTestConfig: Using LDAP on urls={}, basePath={}, managerDn={}",
            ret.getUrls(), ret.getBaseLdapPathAsString(), ret.getUserDn());
        return ret;
    }

    // @Bean
    BindAuthenticator authenticator(BaseLdapPathContextSource contextSource) {
        BindAuthenticator authenticator = new BindAuthenticator(contextSource);
        authenticator.setUserDnPatterns(new String[]{"cn={0}," + BASE_PATH});
        return authenticator;
    }

    // @Bean
    LdapAuthenticationProvider authenticationProvider(LdapAuthenticator authenticator) {
        return new LdapAuthenticationProvider(authenticator);
    }
}
