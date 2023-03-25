package com.giraone.webmvc.security;

import com.giraone.webmvc.security.config.WebSecurityTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(WebSecurityTestConfig.class)
class SecurityDemoApplicationTests {

    @Test
    void contextLoads() {
    }

}
