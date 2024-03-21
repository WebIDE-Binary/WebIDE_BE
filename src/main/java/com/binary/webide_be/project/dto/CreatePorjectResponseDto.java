package com.binary.webide_be.project.dto;


import com.binary.webide_be.project.entity.Project;
import com.binary.webide_be.project.entity.ProjectLanguagesEnum;
import com.binary.webide_be.team.entity.Team;
import com.binary.webide_be.user.entity.User;
import lombok.Data;
import lombok.Getter;

@Getter
public class CreatePorjectResponseDto {

    private Long projectId;
    private ProjectLanguagesEnum projectLanguagesEnum;
    private String projectName;
    private String projectDesc;

    private Long teamId;
    private String teamName;

    private Long userId;
    private String nickName;
    private String email;
    private String profileImage;

    public CreatePorjectResponseDto (User user, Team team, Project project) { //매개변수를 만들어둔 객체로 받음
        this.projectId = project.getProjectId();
        this.projectLanguagesEnum = project.getProjectLanguagesEnum();
        this.projectName = project.getProjectName();
        this.projectDesc = project.getProjectDesc();
        this.teamId = team.getTeamId();
        this.teamName = team.getTeamName();
        this.userId = user.getUserId();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImg();
    }

}
