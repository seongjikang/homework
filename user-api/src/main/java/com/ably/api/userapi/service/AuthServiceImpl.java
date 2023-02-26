package com.ably.api.userapi.service;

import com.ably.api.userapi.dto.AuthDto;
import com.ably.api.userapi.vo.RequestAuth;
import com.ably.api.userapi.vo.ResponseAuth;
import com.ably.api.userapi.vo.ResponseAuthNo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{
    Environment env;
    RestTemplate restTemplate;
    @Override
    public ResponseEntity<ResponseAuthNo> requestAuth(String phoneNo) {
        String smsServerUrl = String.format("%s/auth/request",env.getProperty("sms.url"));
        RequestAuth request = new RequestAuth();
        request.setPhoneNo(phoneNo);
        ResponseEntity<ResponseAuthNo> response =
                restTemplate.postForEntity(smsServerUrl,
                        request,
                        ResponseAuthNo.class);
        return response;
    }

    @Override
    public AuthDto auth(String phoneNo, String authNo) {

        if(!authNo.equals(env.getProperty("sms.auth_no"))) {
            throw new RuntimeException("인증번호가 일치하지 않습니다.");
        }

        String token = Jwts.builder()
                .setSubject(phoneNo)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("auth_token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("auth_token.secret"))
                .compact();

        if(token == null) {
            throw new RuntimeException("휴대폰 인증이 실패하였습니다.");
        }

        return new AuthDto(phoneNo, authNo, token);
    }
}
