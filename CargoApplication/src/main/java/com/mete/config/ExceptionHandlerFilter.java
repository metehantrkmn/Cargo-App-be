package com.mete.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        try{
            //extract calims method of jwt service checks if jwt token expired or not
            //if that method throws expired jwt exception then catch block handle it
            filterChain.doFilter(request,response);
        }catch (Exception ex){
            System.out.println("exception occured uring one of the filters");
            response.setStatus(403);
            ex.printStackTrace();
        }

    }
}
