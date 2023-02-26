package com.ably.api.userapi.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class RequestLogin {
    @NotNull(message = "Id cannot be null")
    @Size(min = 2, message = "Id not be less than two characters.")
    private String id;
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be equal or grater than 8 characters")
    private String password;

}
