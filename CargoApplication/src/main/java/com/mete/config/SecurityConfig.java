package com.mete.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationFilter authenticationFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final ServiceAuthenticationFilter serviceAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> {
                    auth.requestMatchers("/api/cargo/get-cargo-record").permitAll();
                    auth.requestMatchers(
                            "/api/delivery/**"
                                    , "/api/return/**"
                                    ,"/api/btob/**"
                    ).hasAnyRole("MANAGER","PERSONEL");
                    auth.requestMatchers("/api/branch/**").hasAnyRole("MANAGER");
                    auth.requestMatchers("/api/personel/**","/api/user/**").hasRole("ADMIN");
                    auth.requestMatchers("/api/cargo/**").hasAnyRole("MANAGER","PERSONEL","USER");
                    auth.anyRequest().authenticated();
                })
                .sessionManagement((session) -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(serviceAuthenticationFilter,AuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, ServiceAuthenticationFilter.class)
                .cors(Customizer.withDefaults());
        return http.build();
    }

}
