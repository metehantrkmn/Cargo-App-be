package com.mete.userInfo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo,String> {

    Optional<UserInfo> findUserInfoByUserId(String id);

}
