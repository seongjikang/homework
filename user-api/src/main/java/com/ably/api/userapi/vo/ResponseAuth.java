package com.ably.api.userapi.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseAuth {
    private String authNo;
    private String phoneNo;
    private String authKey;
}
