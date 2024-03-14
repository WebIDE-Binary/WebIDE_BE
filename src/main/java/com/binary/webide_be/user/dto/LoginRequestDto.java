package com.binary.webide_be.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String email;
    private String password;
}
