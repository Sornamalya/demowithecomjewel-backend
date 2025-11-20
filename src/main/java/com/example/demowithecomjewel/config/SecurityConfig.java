package com.example.demowithecomjewel.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})  // enable CORS
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers("/**").permitAll() // allow all requests
                .requestMatchers("/api/auth/**").permitAll()        // login/register public
                .requestMatchers("/api/products/**").permitAll()    // products public
                .requestMatchers("/uploads/**").permitAll()         // static files
                .requestMatchers("/api/users/**").permitAll()       // allow cart/wishlist without Spring auth
                .anyRequest().authenticated()
            )
            .formLogin().disable()    // disable default login form
            .httpBasic().disable();   // disable browser basic auth prompt

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
