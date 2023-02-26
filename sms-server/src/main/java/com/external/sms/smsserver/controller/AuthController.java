package com.external.sms.smsserver.controller;

import com.external.sms.smsserver.vo.RequestAuth;
import com.external.sms.smsserver.vo.ResponseAuth;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
@AllArgsConstructor
public class AuthController {
    Environment env;

    @PostMapping("/request")
    public ResponseEntity<ResponseAuth> requestAuthNo(@RequestBody RequestAuth request) {
        ResponseAuth response = new ResponseAuth(env.getProperty("sms.auth_no"),request.getPhoneNo());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }




}
