package com.binary.webide_be.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @CustomEmail
    @NotEmpty
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+=\\-`])[A-Za-z\\d~!@#$%^&*()_+=\\-`]{8,15}$", message = "비밀번호는 영문 대/소문자, 숫자, 특수문자(~!@#$%^&*()_+=\\-`)를 적어도 1개씩 포함하여 8자 ~ 15자여야 합니다.")
    @NotEmpty
    private String password;

    @NotEmpty
    private String passwordCheck;
//    @Pattern(regexp = "^([ㄱ-ㅎ|ㅏ-ㅣ|가-힣0-9]{1,6}|[a-zA-Z0-9]{1,8})$")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,12}$", message = "닉네임은 한글과 영문 대/소문자, 숫자로 이루어진 2자 ~ 12글자 여야 합니다.")
    @NotEmpty
    private String nickName;
}
