package com.mete.config;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.sql.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Data
public class AuthenticationFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate;

    public record UserRecord(
            String userId,
            String role,
            String email,
            String phoneNumber,
            String name,
            String surname,
            String SEX,
            String password,
            Date registrationDate
    ){}
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if(SecurityContextHolder.getContext().getAuthentication() != null){
            System.out.println("authentication already set continue to filter chain");
            filterChain.doFilter(request,response);
            return;
        }

        String apiUrl = "http://your-java-app:8080/api/v1/verify";
        //Bearer myAccessToken
        String authorizationToken = request.getHeader("Authorization");

        System.out.println("filtre basladi");

        if(authorizationToken == null){
            filterChain.doFilter(request,response);
            return;
        }

        System.out.println("filtre devam ediyor 1");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        // Perform the GET request with headers
        ResponseEntity<UserRecord> restResponse = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                httpEntity,
                UserRecord.class
        );
        System.out.println(restResponse);

        if(restResponse.getBody() == null){
            filterChain.doFilter(request,response);
            return;
        }


        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                restResponse.getBody().userId,
                restResponse.getBody().password,
                List.of(new SimpleGrantedAuthority("ROLE_" + restResponse.getBody().role))
        );

        authToken.setDetails(
                restResponse.getBody()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
        System.out.println("filtre bitti");
        filterChain.doFilter(request,response);
    }
}
