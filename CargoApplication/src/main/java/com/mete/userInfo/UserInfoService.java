package com.mete.userInfo;

import com.mete.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final RestService restService;

    public String addUser(UserController.UserRecord user){

        //todo => send email about registration

        UserInfo userInfo = UserInfo.builder()
                .userId(user.userId())
                .email(user.email())
                .name(user.name())
                .build();

        userInfoRepository.save(userInfo);

        restService.sendRequestToMailingService(
            new RestService.MailingRecord(
                    "routingMailing",
                    userInfo.getEmail(),
                    userInfo.getName(),
                    null,
                    1
            )
        );

        return "new user succesfuly added";
    }

    public ResponseEntity deleteUser(String userId){

        if(!userInfoRepository.existsById(userId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");

        userInfoRepository.deleteById(userId);

        return ResponseEntity.ok("succesfully deleted the user");
    }

}
