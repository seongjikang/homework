package com.ably.api.userapi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String nickName;
    private String phoneNo;

    private String userId;
    private Date createdAt;
    private String encryptedPwd;
}

