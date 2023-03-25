package com.giraone.webflux.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerAdapter;
import org.springframework.security.config.ldap.LdapPasswordComparisonAuthenticationManagerFactory;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import javax.naming.NamingException;
import java.util.Hashtable;

@Configuration
public class LdapConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(LdapConfig.class);

    private final LdapProperties ldapProperties = new LdapProperties();

    @ConditionalOnMissingBean
    @Bean
    ContextSource contextSource() {
        final String providerUrl = ldapProperties.getLdapUrl() + "/" + ldapProperties.getBasePath();
        final LdapContextSource ret = new DefaultSpringSecurityContextSource(providerUrl);
        final String managerDn = ldapProperties.getBindUser().indexOf(',') > 0
            ? ldapProperties.getBindUser()
            : ldapProperties.getBindUser() + "," + ldapProperties.getBasePath();
        ret.setUserDn(managerDn);
        ret.setPassword(ldapProperties.getBindPassword());
        LOGGER.info("Using LDAP on urls={}, basePath={}, managerDn={}",
            ret.getUrls(), ret.getBaseLdapPathAsString(), ret.getUserDn());
        return ret;
    }

    @Bean
    AuthenticationManager authenticationManager(BaseLdapPathContextSource contextSource,
                                                LdapAuthoritiesPopulator authorities,
                                                PasswordEncoder passwordEncoder) {

        if (LOGGER.isInfoEnabled()) {
            try {
                LOGGER.info("Using authenticationManager with nameInNamespace={}",
                    contextSource.getReadOnlyContext().getNameInNamespace()
                );
                Hashtable<?, ?> env = contextSource.getReadOnlyContext().getEnvironment();
                env.forEach((k, v) -> LOGGER.info("- LDAP env {}={}", k,
                    k.toString().contains("credentials") ? hideCredentials(v) : v));
            } catch (NamingException e) {
                LOGGER.error("Cannot get details for {}", contextSource.getReadOnlyContext());
            }
        }

        LdapPasswordComparisonAuthenticationManagerFactory factory = new LdapPasswordComparisonAuthenticationManagerFactory(
            contextSource, passwordEncoder);

        // (1) User search
        final String userDnPatterns = ldapProperties.getUserSearch().getUserDnPatterns();
        if (userDnPatterns != null && userDnPatterns.length() > 0) {
            // directly access via DN
            factory.setUserDnPatterns(userDnPatterns);
        } else {
            // indirect access via search query
            factory.setUserSearchBase(ldapProperties.getUserSearch().getUserSearchBase());
            factory.setUserSearchFilter(ldapProperties.getUserSearch().getUserSearchFilter());
        }

        // (2) Password authentication
        final String passwordAttribute = ldapProperties.getUserSearch().getPasswordAttribute();
        factory.setPasswordAttribute(passwordAttribute);
        // (3a) Map UserDetails
        // factory.setUserDetailsContextMapper(userDetailsContextMapper());

        // (3) Fetch authorities
        factory.setLdapAuthoritiesPopulator(authorities);

        LOGGER.info("Using LDAP search with userDnPatterns={}, passwordAttribute={}", userDnPatterns, passwordAttribute);

        return factory.createAuthenticationManager();
    }

    @Bean
    DefaultLdapAuthoritiesPopulator authorities(BaseLdapPathContextSource contextSource) {

        // (3) Fetch authorities / groups
        final String groupSearchBase = ldapProperties.getGroupSearch().getGroupSearchBase();
        DefaultLdapAuthoritiesPopulator authorities =
            new DefaultLdapAuthoritiesPopulator(contextSource, groupSearchBase);
        final String groupSearchFilter = ldapProperties.getGroupSearch().getGroupSearchFilter();
        authorities.setGroupSearchFilter(groupSearchFilter);
        LOGGER.info("Using LDAP group search with groupSearchBase={}, groupSearchFilter={}",
            groupSearchBase, groupSearchFilter);
        return authorities;
    }

    @Bean
    ReactiveAuthenticationManager reactiveAuthenticationManager(AuthenticationManager authenticationManager) {

        LOGGER.info("Using ReactiveAuthenticationManager");
        return new ReactiveAuthenticationManagerAdapter(authenticationManager);
    }

    // See https://springhow.com/spring-security-password-encoder/
    @Bean
    PasswordEncoder passwordEncoder() {

        return NoOpPasswordEncoder.getInstance();
        // PasswordEncoder passwordEncoder = new LdapShaPasswordEncoder();
        /*
        LOGGER.info("Using adapted DelegatingPasswordEncoder");
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("SHA", new MessageDigestPasswordEncoder("SHA-1"));
        return new DelegatingPasswordEncoder("SHA", encoders);
        */
    }

    /*
    @Bean
    public UserDetailsContextMapper userDetailsContextMapper() {
        return new LdapUserDetailsMapper()
        {
            @Override
            public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
                Attributes attributes = ctx.getAttributes();
                LdapUserDetails ldapUserDetails = (LdapUserDetails) super.mapUserFromContext(ctx, username, authorities);
                String surName = ldapUserDetails.getUsername();
                try {
                    surName = attributes.get("sn").get().toString();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
                return new MyLdapUser(ldapUserDetails, surName);
            }
        };
    }
     */

    private static String hideCredentials(Object value) {
        return new String(new char[value.toString().length()]).replace('\0', '*');
    }
}
