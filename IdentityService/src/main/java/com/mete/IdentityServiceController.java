package com.mete;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class IdentityServiceController {

    private final IdentityService identityService;

    public record CheckBody(String identityNumber){}
    @PostMapping("/IdentityService/v1/check")
    public boolean check(@RequestBody CheckBody body){
        return identityService.check(body.identityNumber);
    }

    @GetMapping("/IdentityService/v1/get-user")
    public ResponseEntity getUser(@RequestParam(name = "identityNo") String identityNo){
        return identityService.getUser(identityNo);
    }

    @GetMapping("/IdentityService/v1/get-user-byNameSurname")
    public ResponseEntity getUserByNameSurname(@RequestParam(name = "name") String name,
                                               @RequestParam(name = "surname") String surname){
        return identityService.getUserByNameSurname(name, surname);
    }
}
