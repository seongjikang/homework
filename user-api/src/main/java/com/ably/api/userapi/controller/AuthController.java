package com.ably.api.userapi.controller;

import com.ably.api.userapi.dto.AuthDto;
import com.ably.api.userapi.service.AuthService;
import com.ably.api.userapi.vo.RequestAuth;
import com.ably.api.userapi.vo.RequestAuthNo;
import com.ably.api.userapi.vo.ResponseAuth;
import com.ably.api.userapi.vo.ResponseAuthNo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class AuthController {
    AuthService authService;
    @PostMapping("/auth/request")
    public ResponseEntity<ResponseAuthNo> requestAuth(@RequestBody RequestAuthNo request) {
        return authService.requestAuth(request.getPhoneNo());
    }

    @PostMapping("/auth")
    public ResponseEntity<ResponseAuth> requestAuth(@RequestBody RequestAuth request) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        AuthDto authDto =  authService.auth(request.getPhoneNo(), request.getAuthNo());

        ResponseAuth responseAuth = mapper.map(authDto, ResponseAuth.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseAuth);
    }

    @GetMapping("/status")
    public String status() {
        return "STATUS";
    }

}
