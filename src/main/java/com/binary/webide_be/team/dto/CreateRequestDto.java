package com.binary.webide_be.team.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class CreateRequestDto<T> {

    @NotEmpty
    private String teamName;
    private List<Long> participants = new ArrayList<>();

}
