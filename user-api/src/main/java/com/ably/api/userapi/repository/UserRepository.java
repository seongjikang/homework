package com.ably.api.userapi.repository;

import com.ably.api.userapi.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmailOrPhoneNo(String email, String phoneNo);

    UserEntity findByEmail(String email);
}
