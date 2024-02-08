package com.mete.userInfo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo,String> {

    UserDetails findUserInfoByName(String username);
    Optional<UserInfo> findUserInfoByUserId(String userId);

    List<UserInfo> findUserInfoByRole(ROLE role);

    boolean existsUserInfoByUserId(String userId);
    Optional<UserInfo> findUserInfoByEmail(String email);
    boolean existsUserInfoByEmail(String email);

}
