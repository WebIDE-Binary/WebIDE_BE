package com.binary.webide_be.team.dto;


import com.binary.webide_be.team.entity.Team;
import com.binary.webide_be.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.aspectj.bridge.IMessageContext;
import org.springframework.http.HttpStatusCode;

import static jdk.internal.joptsimple.internal.Messages.message;

public class CreateResponseDto {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private  int statusCode;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message(String s) {
        return message("팀 생성 성공!");
    };



}
