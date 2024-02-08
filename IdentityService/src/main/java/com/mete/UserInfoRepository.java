package com.mete;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo,String> {

    UserInfo findUserInfoByName(String name);
    Optional<UserInfo> findUserInfoByIdentity(String Identity);
    boolean existsUserInfoByIdentity(String Identity);
    Optional<UserInfo> findUserInfoByNameAndSurname(String name, String surname);

}