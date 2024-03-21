package com.binary.webide_be.user.controller;

import com.binary.webide_be.user.dto.UpdateUserInfoRequestDto;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.user.dto.CustomEmail;
import com.binary.webide_be.user.dto.LoginRequestDto;
import com.binary.webide_be.user.dto.SignupRequestDto;
import com.binary.webide_be.user.service.UserService;
import com.binary.webide_be.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @Operation(summary = "회원 정보 수정", description = "[회원 정보 수정] api")
    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<?>> updateUserInfo(@RequestPart(value = "updateUserInfoRequestDto") UpdateUserInfoRequestDto updateUserInfoRequestDto,
                                                         @RequestPart(value = "profileImage",required = false) MultipartFile profileImage,
                                                         @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        return ResponseEntity.ok(userService.updateUserInfo(updateUserInfoRequestDto, profileImage, userDetails));
    }
}
