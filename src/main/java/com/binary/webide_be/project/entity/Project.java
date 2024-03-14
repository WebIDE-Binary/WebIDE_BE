package com.binary.webide_be.project.entity;

import com.binary.webide_be.util.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Project extends TimeStamped {

    @Id @GeneratedValue
    @Column
    private Long id;

    @Column(nullable = false)
    private String projectName; //프로젝트 이름

    @Column
    private String projectDesc; //프로젝트 설명

    @Column(nullable = false)
    private String projectSelectLanguage; //프로젝트 선택언어

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team team; //연관관계 설정, 어떤 필드 검사? 를 비활성화 시켰더니 빨간줄이 없어졌다.


}
