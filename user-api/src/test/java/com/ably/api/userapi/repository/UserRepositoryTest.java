package com.ably.api.userapi.repository;

import com.ably.api.userapi.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @MockBean
    RestTemplateBuilder restTemplateBuilder;

    @Test
    void findByEmailOrPhoneNoTest() {
        UserEntity entity = new UserEntity();
        entity.setEmail("abcd123@gmail.com");
        entity.setPhoneNo("01011112222");
        entity.setEncryptedPwd("encryptpassword");
        entity.setName("kang");
        entity.setNickName("nickname");

        UserEntity createUser =userRepository.save(entity);

        UserEntity findUserByEmail = userRepository.findByEmailOrPhoneNo("abcd123@gmail.com", "abcd123@gmail.com");
        UserEntity findUserByPhoneNo = userRepository.findByEmailOrPhoneNo("01011112222", "01011112222");

        assertEquals(findUserByEmail,findUserByPhoneNo);
    }
}