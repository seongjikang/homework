package com.external.sms.smsserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseAuth {
    private String authNo;
    private String phoneNo;
}
