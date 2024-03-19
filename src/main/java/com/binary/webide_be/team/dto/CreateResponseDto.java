package com.binary.webide_be.team.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateResponseDto {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private  int statusCode;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message(String s) {
        return message("팀 생성 성공!");
    };



}
