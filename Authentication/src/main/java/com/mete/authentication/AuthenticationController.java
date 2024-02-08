package com.mete.authentication;

import com.mete.userInfo.ROLE;
import com.mete.userInfo.SEX;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public record RegisterBody(
            String UserId,
            ROLE role,
            String email,
            String phoneNumber,
            String name,
            String surname,
            SEX sex,
            String password
    ){}
    @PostMapping("/api/v1/register")
    public ResponseEntity register(@RequestBody RegisterBody body){
        return authenticationService.registerService(body);
    }

    public record LoginBody(String email, String password){}
    @PostMapping("/api/v1/login")
    public ResponseEntity login(@RequestBody LoginBody body){
        return authenticationService.login(body);
    }

    @GetMapping("/api/v1/verify")
    public ResponseEntity verify(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return authenticationService.verify(authorizationHeader);
    }


}
