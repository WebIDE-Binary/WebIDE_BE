package com.binary.webide_be.project.dto;

import com.binary.webide_be.project.entity.ProjectLanguagesEnum;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Data
public class CreateProjectRequestDto {
    private Long userId;
    private ProjectLanguagesEnum projectLanguagesEnum;
    private String projectName;
    private String projectDesc;

    private String teamName;

    private List<Long> participantIds = new ArrayList<>(); //팀원들이 ID를 배열로 받아오기 (이렇게 하는게 맞나?)
}
