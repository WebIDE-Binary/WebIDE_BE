package com.binary.webide_be.user.controller;

import com.binary.webide_be.user.dto.CustomEmail;
import com.binary.webide_be.user.dto.LoginRequestDto;
import com.binary.webide_be.user.dto.SignupRequestDto;
import com.binary.webide_be.user.service.UserService;
import com.binary.webide_be.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    //회원가입
    @Operation(summary = "회원가입", description = "[회원가입] api")
    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<?>> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(userService.signup(signupRequestDto));
    }

    //이메일 중복 체크
    @Operation(summary = "회원가입 - 이메일 중복 체크", description = "[이메일 중복 체크] api")
    @PostMapping("/signup/{email}")
    public ResponseEntity<ResponseDto<?>> checkEmail(@PathVariable @CustomEmail String email) {
        return ResponseEntity.ok(userService.emailCheck(email));
    }

    //로그인
    @Operation(summary = "일반 로그인", description = "[일반 로그인] api")
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<?>> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        return ResponseEntity.ok(userService.login(loginRequestDto, response));
    }
}
