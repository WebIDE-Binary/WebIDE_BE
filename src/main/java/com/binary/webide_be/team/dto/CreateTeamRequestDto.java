package com.binary.webide_be.team.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateTeamRequestDto {

    @NotEmpty
    private String teamName;

    @NotNull
    private List<Long> participants = new ArrayList<>();

}