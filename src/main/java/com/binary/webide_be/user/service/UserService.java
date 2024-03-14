package com.binary.webide_be.user.service;

import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.exception.message.SuccessMsg;
import com.binary.webide_be.jwt.JwtUtil;
import com.binary.webide_be.user.dto.LoginRequestDto;
import com.binary.webide_be.user.dto.LoginResponseDto;
import com.binary.webide_be.user.dto.SignupRequestDto;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.user.entity.UserRoleEnum;
import com.binary.webide_be.user.repository.UserRepository;
import com.binary.webide_be.util.dto.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

import static com.binary.webide_be.exception.message.ErrorMsg.*;
import static com.binary.webide_be.exception.message.SuccessMsg.*;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //회원가입
    @Transactional
    public ResponseDto<?> signup(
            @Valid SignupRequestDto signupRequestDto
    ) {
        if (!signupRequestDto.getPassword().equals(signupRequestDto.getPasswordCheck())){
            throw new CustomException(PASSWORD_INCORRECT_MISMATCH);
        }
        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickName = signupRequestDto.getNickName();;

        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()) {
            throw new CustomException(DUPLICATE_USER);
        }
        User user = new User(email, nickName, password);
        userRepository.save(user);
        return ResponseDto.builder()
                .statusCode(SIGN_UP_SUCCESS.getHttpStatus().value())
                .message(SIGN_UP_SUCCESS.getDetail()).build();
    }

    //이메일 중복 검사
    public ResponseDto<?> emailCheck(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        if(findUser.isPresent()) {
            User user = findUser.get();
            if (user.getEmail().equals(email)){
                throw new CustomException(DUPLICATE_EMAIL);
            }
        }
        return ResponseDto.builder()
                .statusCode(EMAIL_CHECK_SUCCESS.getHttpStatus().value())
                .message(EMAIL_CHECK_SUCCESS.getDetail()).build();
    }

    //로그인
    @Transactional
    public ResponseDto<?> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(PASSWORD_INCORRECT);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getEmail(), UserRoleEnum.USER));
        return ResponseDto.builder()
                .statusCode(LOGIN_SUCCESS.getHttpStatus().value())
                .message(LOGIN_SUCCESS.getDetail())
                .data(new LoginResponseDto(user))
                .build();
    }
}
