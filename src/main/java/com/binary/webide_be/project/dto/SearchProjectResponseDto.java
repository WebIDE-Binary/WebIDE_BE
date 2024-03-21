package com.binary.webide_be.project.dto;

import com.binary.webide_be.project.entity.Project;
import com.binary.webide_be.project.entity.ProjectLanguagesEnum;
import com.binary.webide_be.team.entity.UserTeam;
import com.binary.webide_be.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SearchProjectResponseDto {

    private Long projectId;
    private Long teamId;
    private String teamName;
    private List<String> userProfileImage = new ArrayList<>();
    private String projectName;
    private String projectDesc;
    private ProjectLanguagesEnum projectLanguagesEnum;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public SearchProjectResponseDto(Project project, List<String> userProfileImage) {
        this.projectId = project.getProjectId();
        this.teamId = project.getTeam().getTeamId();
        this.teamName = project.getTeam().getTeamName();
        this.userProfileImage = userProfileImage;
        this.projectName = project.getProjectName();
        this.projectDesc = project.getProjectDesc();
        this.projectLanguagesEnum = project.getProjectLanguagesEnum();
        this.createdAt = project.getCreatedAt();
        this.modifiedAt = project.getModifiedAt();
    }

}
