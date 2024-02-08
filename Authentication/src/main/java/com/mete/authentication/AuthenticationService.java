package com.mete.authentication;

import com.mete.userInfo.ROLE;
import com.mete.userInfo.UserInfo;
import com.mete.userInfo.UserInfoRepository;
import com.mete.authentication.exceptions.NoSuchUserExists;
import com.mete.authentication.token.Token;
import com.mete.authentication.token.TokenRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserInfoRepository userInfoRepository;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final TokenSerivce tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    @Value("${authentication.service.token}")
    private String token;

    public ResponseEntity registerService(AuthenticationController.RegisterBody body) {

        //if user already exists it will no continue to registration proccess
        if(userInfoRepository.existsUserInfoByUserId(body.UserId()) || userInfoRepository.existsUserInfoByEmail(body.email()))
            return ResponseEntity.badRequest().body("User Already Exists");



        UserInfo user = UserInfo.builder()
                .email(body.email())
                .userId(body.UserId() != null ? body.UserId() : UserInfo.generateRandomNumberString(11))
                .sex(body.sex())
                .role(body.role())
                .phoneNumber(body.phoneNumber())
                .name(body.name())
                .surname(body.surname())
                .password(passwordEncoder.encode(body.password()))
                .registrationDate(Date.valueOf(LocalDate.now()))
                .build();

        //send request to the Identity check service in order to controll whether this Id realiy exists
        //before sending Identity controll if role is user or personell. if personell no need to check identity
        //todo


        //send request to the source application so that user data kept synchronized
        //todo
        String apiUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Service " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        System.out.println("before requests");
        if (user.getRole().name().equals("USER")) {
            apiUrl = "http://cargo-source-app:8080/api/user/add-user";
            System.out.println("role = user");

            // Updated: Use a consistent Map for request body
            Map<String, String> requestBody = Map.of(
                    "userId", user.getUserId(),
                        "email", user.getEmail()
            );

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            try {
                String response = restTemplate.postForObject(apiUrl, requestEntity, String.class);
                System.out.println("Successfully added a new user. Response: " + response);
            } catch (RestClientException e) {
                // Handle RestClientException (e.g., timeouts, network issues)
                System.err.println("Error adding a new user: " + e.getMessage());
            }

        } else if (user.getRole().name().equals("PERSONEL") || user.getRole().name().equals("MANAGER")) {
            apiUrl = "http://cargo-source-app:8080/api/personel/add-personel";
            System.out.println("role = personel or manager");

            // Updated: Use a consistent Map for request body
            Map<String, String> requestBody = Map.of("personelId", user.getUserId());

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            try {
                String response = restTemplate.postForObject(apiUrl, requestEntity, String.class);
                System.out.println("Successfully added a new personel. Response: " + response);
            } catch (RestClientException e) {
                // Handle RestClientException (e.g., timeouts, network issues)
                System.err.println("Error adding a new personel: " + e.getMessage());
            }
        }
        System.out.println("after requests");


        //send request to the mailing service. Acknowledge them that they registered succesfully
        //this proccess is asynchronized and being done with rabbitmq
        //todo

        userInfoRepository.save(user);

        return ResponseEntity.ok("Registration is Succesfull!!!");
    }

    public void revokeAllActiveTokens(String userId){
        List<Token> tokens = tokenRepository.getActiveTokens(userId);
        for (Token t: tokens){
            t.setRevoked(true);
        }
        tokenRepository.saveAll(tokens);
    }


    public ResponseEntity login(AuthenticationController.LoginBody requestBody){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestBody.email(),
                        requestBody.password()
                )
        );

        //if no exception occured then user authenticated succesfully and rest of the operations continues
        UserInfo user = userInfoRepository.findUserInfoByEmail(requestBody.email())
                .orElseThrow(() ->
                        new NoSuchUserExists("No such user exists!!!")
                );

        //create a new jwt token using jwt service and return it back to user
        String token = tokenService.generateToken(user);

        //first revoke token in use
        revokeAllActiveTokens(user.getUserId());
        //the  save the new token
        saveToken(user,token);

        ResponseEntity resp = ResponseEntity.ok().header("Authorization","Bearer " + token).body(token);

        return resp;
    }

    public void saveToken(UserInfo user, String jwtToken){
        Token token = Token.builder()
                .user(user)
                .revoked(false)
                .token(jwtToken)
                .build();
        tokenRepository.save(token);
    }

    public ResponseEntity verify(String authorizationHeader){

        if(authorizationHeader == null)
            return ResponseEntity.badRequest().body("missing authorization header");

        String token = authorizationHeader.substring(7);

        if(token == null)
            return ResponseEntity.badRequest().body("missing Token");

        String userName = tokenService.extractUsername(token);

        if(userName != null){
            UserDetails user = userDetailsService.loadUserByUsername(userName);
            //NoTokenFindException
            Token jwt = tokenRepository.findTokenByToken(token).orElseThrow();

            if(tokenService.isTokenValid(token,user) && !jwt.isRevoked() && user.isEnabled()){
                //send request to calling service back that token is valid
                return ResponseEntity.ok(user);
            }

        }
        return ResponseEntity.badRequest().body("token is not valid");
    }




}
