package com.mete.config;

import com.mete.authentication.token.Token;
import com.mete.authentication.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomLogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        Token jwt = tokenRepository.findTokenByToken(token).orElseThrow(
                () -> new NoSuchTokenExists("no such token exists")
        );

        jwt.setRevoked(true);
        tokenRepository.save(jwt);
        System.out.println("succesfully logout!!!!");
    }
}
