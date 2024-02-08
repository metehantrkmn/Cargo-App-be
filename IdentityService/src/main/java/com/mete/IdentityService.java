package com.mete;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdentityService {

    private final UserInfoRepository userInfoRepository;

    public boolean check(String identityNumber){
        return userInfoRepository.existsUserInfoByIdentity(identityNumber);
    }


    public record IdentityRecord(
            String identity,
            String name,
            String surname,
            String email
    ){}
    public ResponseEntity getUser(String identityNo){
        UserInfo user = userInfoRepository.findUserInfoByIdentity(identityNo).orElseThrow(
                () -> new UserNotExists("this user not exists")
        );
        IdentityRecord newRecord = new IdentityRecord(
                user.getIdentity(),
                user.getName(),
                user.getSurname(),
                user.getEmail()
        );

        System.out.println("Identity debug getUser");
        System.out.println(newRecord);

        return ResponseEntity.ok(newRecord);
    }

    public ResponseEntity  getUserByNameSurname(String name, String surname){
        UserInfo user = userInfoRepository.findUserInfoByNameAndSurname(name,surname).orElseThrow(
                () -> new UserNotExists("this user not exists")
        );
        IdentityRecord newRecord = new IdentityRecord(
                user.getIdentity(),
                user.getName(),
                user.getSurname(),
                user.getEmail()
        );

        System.out.println("Identity debug getUserByNameSurname");
        System.out.println(newRecord);

        return ResponseEntity.ok(newRecord);
    }

}
