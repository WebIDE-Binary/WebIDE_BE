package com.binary.webide_be.project.dto;

import com.binary.webide_be.project.entity.Project;
import com.binary.webide_be.project.entity.ProjectLanguagesEnum;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class FindProjectResponseDto {

    private Long projectId;
    private Long teamId;
    private String teamName;
    private List<String> userProfileImage = new ArrayList<>();
    private String projectName;
    private String projectDesc;
    private ProjectLanguagesEnum projectLanguagesEnum;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public FindProjectResponseDto(Project project, List<String> userProfileImage) {
        this.projectId = project.getProjectId();
        this.userProfileImage = userProfileImage;
        this.projectName = project.getProjectName();
        this.projectDesc = project.getProjectDesc();
        this.projectLanguagesEnum = project.getProjectLanguagesEnum();
        this.createdAt = project.getCreatedAt();
        this.modifiedAt = project.getModifiedAt();
    }

}
