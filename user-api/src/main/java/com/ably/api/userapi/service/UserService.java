package com.ably.api.userapi.service;


import com.ably.api.userapi.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(String authKey, UserDto userDto);

    UserDto getUserDetailsByEmail(String user);

    UserDto updatePassword(String authKey, UserDto userDto);
}
