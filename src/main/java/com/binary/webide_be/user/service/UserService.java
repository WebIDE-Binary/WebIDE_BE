package com.binary.webide_be.user.service;

import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.jwt.JwtUtil;
import com.binary.webide_be.user.dto.*;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.user.entity.UserRoleEnum;
import com.binary.webide_be.user.repository.UserRepository;
import com.binary.webide_be.util.S3Service;
import com.binary.webide_be.util.dto.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final S3Service s3Service;

    //회원가입
    @Transactional
    public ResponseDto<?> signup(
            @Valid SignupRequestDto signupRequestDto
    ) {
        if (!signupRequestDto.getPassword().equals(signupRequestDto.getPasswordCheck())){ //패스워드 같은지 검사 + 예외
            throw new CustomException(PASSWORD_INCORRECT_MISMATCH);
        }
        String email = signupRequestDto.getEmail(); //회원가입에 필요한(요청) 필드들
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickName = signupRequestDto.getNickName();

        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()) { //isPresent: 이미 값이 존재하면 true (중복이면)
            throw new CustomException(DUPLICATE_USER);
        }
        User user = new User(email, nickName, password); //회원 객채로
        userRepository.save(user); //저장
        return ResponseDto.builder()
                .statusCode(SIGN_UP_SUCCESS.getHttpStatus().value()) //스테이터스 코드랑
                .message(SIGN_UP_SUCCESS.getDetail()) //메시지 리턴해줌
                .build();
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
                .message(EMAIL_CHECK_SUCCESS.getDetail())
                .build();
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

    @Transactional
    public ResponseDto<?> updateUserInfo(UpdateUserInfoRequestDto updateUserInfoRequestDto, MultipartFile profileImage, UserDetailsImpl userDetails) throws IOException {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(UNAUTHORIZED_MEMBER)
        );

        String profileImageUrl = null;
        String nickName = updateUserInfoRequestDto.getNickName();
        String email = user.getEmail();
        if (profileImage != null && !profileImage.isEmpty()) {
            profileImageUrl = s3Service.uploadFile(profileImage, "image");
        } else if ((profileImage == null || profileImage.isEmpty()) && (updateUserInfoRequestDto.getDeleteProfile() != null && updateUserInfoRequestDto.getDeleteProfile() == true)) {
            profileImageUrl = null;
        } else if (profileImage == null || profileImage.isEmpty()) {
            profileImageUrl = user.getProfileImg();
        }

        if (nickName == null || nickName.equals("")) { // nickName이 null이면 기존 닉네임으로 설정
            nickName = user.getNickName();
        }

        user.update(nickName, profileImageUrl);
        userRepository.save(user);
        return ResponseDto.builder()
                .statusCode(USER_INFO_UPDATE_SUCCESS.getHttpStatus().value())
                .message(USER_INFO_UPDATE_SUCCESS.getDetail())
                .data(new UpdateUserInfoResponseDto(user, profileImageUrl)) // user 객체 대신 필요한 필드들을 직접 전달
                .build();
    }

}
