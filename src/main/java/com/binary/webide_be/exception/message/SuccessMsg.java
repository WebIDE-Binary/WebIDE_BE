package com.binary.webide_be.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessMsg {

    SIGN_UP_SUCCESS(HttpStatus.CREATED, "회원가입 완료"),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 완료"),
    USER_INFO_UPDATE_SUCCESS(HttpStatus.OK, "회원 정보 수정 완료"),
    EMAIL_CHECK_SUCCESS(HttpStatus.OK,"사용 가능한 이메일입니다."),
    CHAT_HISTORY_SUCCESS(HttpStatus.OK,"채팅 기록 조회 완료"),
    CREATE_TEAM_SUCCESS(HttpStatus.CREATED, "팀 생성 완료"),
    MODIFY_TEAM_SUCCESS(HttpStatus.OK,"팀 수정 완료"),

    CREATE_PROJECT_SUCCESS(HttpStatus.CREATED, "프로젝트 생성 완료"),
    UPDATE_PROJECT_SUCCESS(HttpStatus.OK, "프로젝트 수정 완료"),
    DELETE_PROJECT_SUCCESS(HttpStatus.OK, "프로젝트 삭제 완료"),
    SEARCH_PROJECT_SUCCESS(HttpStatus.OK, "내 프로젝트 목록 검색 완료"),

    FILE_TREE_SUCCESS(HttpStatus.OK, "파일 트리 조회 완료");


    private final HttpStatus httpStatus;
    private final String detail;
}
