package com.binary.webide_be.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessMsg {

    SIGN_UP_SUCCESS(HttpStatus.CREATED, "회원가입 완료"),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 완료"),
    EMAIL_CHECK_SUCCESS(HttpStatus.OK,"사용 가능한 이메일입니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
