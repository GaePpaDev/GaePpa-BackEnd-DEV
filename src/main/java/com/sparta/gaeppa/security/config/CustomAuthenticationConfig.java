package com.sparta.gaeppa.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationConfig {
    @Bean
    @Primary
    public AuthenticationConfiguration authenticationConfig() {
        return new AuthenticationConfiguration();
    }
}
