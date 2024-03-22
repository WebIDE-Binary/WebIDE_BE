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
    INVALID_PARENT_PROJECT(BAD_REQUEST, "선택한 부모 파일이 현재 프로젝트에 속하지 않습니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "인증된 사용자가 아닙니다."),
    NOT_LOGGED_ID(UNAUTHORIZED, "로그인이 되어있지 않습니다."),

    /* 403 FORBIDDEN : 권한 없음 */
    YOU_ARE_NOT_A_MEMBER_OF_THE_PROJECT_TEAM_AND_THEREFORE_CANNOT_PERFORM_THIS_ACTION(FORBIDDEN, "당신은 이 프로젝트 담당하는 팀의 구성원이 아님으로 권한이 없습니다."),
    NO_AUTHORITY_TO_UPDATE_PROJECT(FORBIDDEN, " 리더가 아님으로 프로젝트 업데이트 권한이 없습니다."),
    NO_AUTHORITY_TO_DELETE_PROJECT(FORBIDDEN, "프로젝트 삭제 권한이 없습니다."),
    USER_NOT_IN_PROJECT_TEAM(FORBIDDEN, "프로젝트 팀의 구성원이 아닙니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "사용자가 존재하지 않습니다."),
    CHAT_ROOM_NOT_FOUND(NOT_FOUND, "채팅방이 존재하지 않습니다."),
    PROJECT_NOT_FOUND(NOT_FOUND, "프로젝트를 찾을 수 없습니다." ),
    TEAM_NOT_FOUND(NOT_FOUND, "팀이 존재하지 않습니다."),
    PARENT_FILE_NOT_FOUND(NOT_FOUND, "상위 파일 데이터를 찾을 수 없습니다."),
    CHATROOM_NOT_FOUND(NOT_FOUND, "팀에 연결된 채팅방이 없습니다."),
    FILE_NOT_FOUND(NOT_FOUND, "파일 데이터를 찾을 수 없습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_USER(CONFLICT,"이미 가입된 사용자입니다."),
    DUPLICATE_EMAIL(CONFLICT,"중복된 이메일입니다.");


    private final HttpStatus httpStatus;
    private final String detail;
}
