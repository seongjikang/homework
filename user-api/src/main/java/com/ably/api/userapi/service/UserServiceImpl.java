package com.ably.api.userapi.service;

import com.ably.api.userapi.dto.UserDto;
import com.ably.api.userapi.entity.UserEntity;
import com.ably.api.userapi.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    Environment env;
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(String authKey, UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        String authorizationHeader = authKey;
        if (authorizationHeader == null) {
            throw new RuntimeException("auth key is not exist");
        }

        String jwt = authorizationHeader.replace("Bearer", "");

        if(!isJwtValid(jwt)) {
            throw new RuntimeException("auth key is not valid");
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);

        UserDto returnValue = mapper.map(userEntity, UserDto.class);

        return returnValue;
    }

    @Override
    @Transactional
    public UserDto updatePassword(String authKey, UserDto userDto) {

        String authorizationHeader = authKey;
        if (authorizationHeader == null) {
            throw new RuntimeException("auth key is not exist");
        }

        String jwt = authorizationHeader.replace("Bearer", "");

        if(!isJwtValid(jwt)) {
            throw new RuntimeException("auth key is not valid");
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = userRepository.findByEmail(userDto.getEmail());

        if(userEntity == null) {
            throw new UsernameNotFoundException("user info is not exist");
        }

        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

        UserDto returnValue = mapper.map(userEntity, UserDto.class);
        return  returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmailOrPhoneNo(username, username);

        if(userEntity == null) {
            throw  new UsernameNotFoundException(username);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject =null;

        try {
            subject = Jwts.parserBuilder().setSigningKey(env.getProperty("auth_token.secret"))
                    .build().parseClaimsJws(jwt).getBody().getSubject();
        } catch (Exception e) {
            returnValue = false;
        }

        if(subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }

    @Override
    public UserDto getUserDetailsByEmail(String user) {
        UserEntity userEntity = userRepository.findByEmail(user);
        if(userEntity == null) {
            throw new UsernameNotFoundException("user info is not exist");
        }
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }
}
