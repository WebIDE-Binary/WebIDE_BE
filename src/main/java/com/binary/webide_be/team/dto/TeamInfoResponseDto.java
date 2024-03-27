package com.binary.webide_be.team.dto;

import com.binary.webide_be.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamInfoResponseDto {

    private Long teamId;
    private String teamName;
    private List<UserInfoDto> participants = new ArrayList<>();

    public TeamInfoResponseDto(Team team, List<UserInfoDto> participants) {
        this.teamName = getTeamName();
        this.teamId = getTeamId();
        this.participants = participants;
    }
}