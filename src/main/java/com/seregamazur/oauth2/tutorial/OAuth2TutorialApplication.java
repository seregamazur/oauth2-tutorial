package com.seregamazur.oauth2.tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OAuth2TutorialApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2TutorialApplication.class, args);
    }

}
