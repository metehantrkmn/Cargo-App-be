package com.mete.userInfo;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserInfoRepository userInfoRepository;
    private final UserInfoService userInfoService;

    /*
    @GetMapping("/api/user/deneme")
    public void deneme(){
        userInfoRepository.save(new UserInfo("13027871706"));
        Optional<UserInfo> mockUser = userInfoRepository.findUserInfoByUserId("13027871706");

        if(mockUser.isPresent())
            System.out.println(mockUser.get());
        System.out.println("this is controller");
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getCredentials());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        System.out.println(mockUser.get());

    }*/

    public record UserRecord(String userId,
                             String email,
                             String name){}
    @PostMapping("/api/user/add-user")
    public String addUser(@RequestBody UserRecord user){
        return userInfoService.addUser(user);
    }

    @DeleteMapping("/api/user/delete-user")
    public ResponseEntity deleteUser(@RequestParam(name = "userId") String userId){
        return userInfoService.deleteUser(userId);
    }

}
