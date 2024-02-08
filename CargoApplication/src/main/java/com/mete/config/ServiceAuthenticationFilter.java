package com.mete.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ServiceAuthenticationFilter extends OncePerRequestFilter {

    @Value("${authentication.service.token}")
    private String token;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        int index= authHeader.indexOf(' ');

        //that means its a bearer token
        //but i am looking for a service key
        if(index != 7){
            filterChain.doFilter(request,response);
            return;
        }

        String serviceToken = authHeader.substring(8);
        System.out.println("service auth token " + serviceToken);

        if(token.equals(serviceToken)){
            System.out.println("authentication has been set in service auth filter");
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    "AUTH_SERVICE",
                    token,
                    List.of(new SimpleGrantedAuthority("ROLE_" + "ADMIN"))
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        System.out.println("sercice authentication suucesfuly come to end");
        filterChain.doFilter(request,response);
    }
}
