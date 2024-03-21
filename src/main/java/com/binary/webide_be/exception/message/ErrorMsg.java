package com.binary.webide_be.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum  ErrorMsg {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    IMAGE_INVALID(BAD_REQUEST,"이미지가 잘못 되었습니다."),
    PASSWORD_INCORRECT(BAD_REQUEST,"비밀번호가 옳지 않습니다."),
    PASSWORD_INCORRECT_MISMATCH(BAD_REQUEST, "입력하신 비밀번호와 비밀번호 확인이 일치하지 않습니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    NOT_LOGGED_ID(UNAUTHORIZED, "로그인이 되어있지 않습니다."),

    /* 403 FORBIDDEN : 권한 없음 */

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "사용자가 존재하지 않습니다."),
    CHAT_ROOM_NOT_FOUND(NOT_FOUND, "채팅방이 존재하지 않습니다."),
    TEAM_NOT_FOUND(NOT_FOUND, "팀이 존재하지 않습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_USER(CONFLICT,"이미 가입된 사용자입니다."),
    DUPLICATE_EMAIL(CONFLICT,"중복된 이메일입니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
