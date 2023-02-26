package com.ably.api.userapi.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestUser {
    @NotNull(message = "Email cannot be null")
    @Size(min = 2, message = "Email not be less than two characters")
    @Email
    private String email;

    @NotNull(message = "NickName cannot be null")
    @Size(min = 2, message = "NickName not be less than two characters")
    private String nickName;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password not be less than eight characters")
    private String pwd;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, message = "Name not be less than two characters")
    private String name;

    @NotNull(message = "phoneNo cannot be null")
    @Size(min = 11, max = 11, message = "Phone Number have to be 11 characters")
    private String phoneNo;


}
