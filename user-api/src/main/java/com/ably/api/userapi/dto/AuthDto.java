package com.ably.api.userapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthDto {
    private String phoneNo;
    private String authNo;
    private String authKey;
}
