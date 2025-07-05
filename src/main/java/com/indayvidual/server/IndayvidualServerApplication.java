package com.indayvidual.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IndayvidualServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IndayvidualServerApplication.class, args);
    }

}
