package com.binary.webide_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WebIdeBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebIdeBeApplication.class, args);
    }

}
