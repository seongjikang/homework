package com.ably.api.userapi.service;


import com.ably.api.userapi.dto.AuthDto;
import com.ably.api.userapi.vo.ResponseAuth;
import com.ably.api.userapi.vo.ResponseAuthNo;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ResponseAuthNo> requestAuth(String phoneNo);

    AuthDto auth(String phoneNo, String authNo);
}
