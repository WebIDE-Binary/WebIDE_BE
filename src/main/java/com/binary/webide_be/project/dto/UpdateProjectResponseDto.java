package com.binary.webide_be.project.dto;

import com.binary.webide_be.project.entity.Project;
import lombok.Getter;

@Getter
public class UpdateProjectResponseDto {

    private Long projectId;
    private String projectName;
    private String projectDesc;


    public UpdateProjectResponseDto(Project project) {
        this.projectId = project.getProjectId();
        this.projectName = project.getProjectName();
        this.projectDesc = project.getProjectDesc();
    }
}
