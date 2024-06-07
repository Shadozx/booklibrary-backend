package com.shadoww.BookLibraryApp;

import com.shadoww.BookLibraryApp.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

@TestConfiguration
public class TestConfig {


    @Bean
    JwtService jwtService() {
        return new JwtService() {
            @Override
            public String extractUserName(String token) {
                return null;
            }

            @Override
            public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
                return null;
            }

            @Override
            public String generateToken(UserDetails userDetails) {
                return null;
            }

            @Override
            public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
                return null;
            }

            @Override
            public boolean isTokenValid(String token, UserDetails userDetails) {
                return false;
            }
        };
    }
}
