package com.binary.webide_be.team.dto;

import lombok.Getter;

@Getter
public class FindTeamResponseDto {

    private Long teamId;
    private String teamName;
    private Long chatRoomId;

    public FindTeamResponseDto(Long teamId, String teamName, Long chatRoomId) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.chatRoomId = chatRoomId;
    }
}
