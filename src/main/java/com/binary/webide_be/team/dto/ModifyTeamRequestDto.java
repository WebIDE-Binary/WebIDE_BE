package com.binary.webide_be.team.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyTeamRequestDto {
    private Long userId; //프로젝트 생성인 ID로, 팀장이 된다

    @NotEmpty
    private String teamName;

    @NotNull
    private List<Long> participants;
}