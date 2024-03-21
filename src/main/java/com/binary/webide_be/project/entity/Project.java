package com.binary.webide_be.project.entity;

import com.binary.webide_be.team.entity.Team;
import com.binary.webide_be.util.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Project extends TimeStamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false)
    private String projectName; //프로젝트 이름

    @Column
    private String projectDesc; //프로젝트 설명

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectLanguagesEnum projectLanguagesEnum; //프로젝트 선택언어

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team team; //연관관계 설정, 어떤 필드 검사? 를 비활성화 시켰더니 빨간줄이 없어졌다.


    public Project(String projectName, String projectDesc, ProjectLanguagesEnum projectLanguagesEnum, Team team) {
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.projectLanguagesEnum = projectLanguagesEnum;
        this.team = team;
    }

    public void update(Long projectId, String projectName, String projectDesc) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDesc = projectDesc;
    }
}
