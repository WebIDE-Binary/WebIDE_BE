package com.binary.webide_be.team.entity;

import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.user.repository.UserRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private int teamSize;

    //Team 객체를 생성할때 @AllArgsConstructor의 영향으로 모든 필드를 넣어서 객체를 생성해야
    // 하지만 아래 생성자를 만들어 놓지 않으면 팀이름, 팀 사이즈만 넣어서 객체를 생성할 수 있음
    public Team(String teamName, int teamSize) {
        this.teamName = teamName;
        this.teamSize = teamSize;
        this.teamId = teamId;
    }

    public void updateTeamName(String newTeamName) {
        this.teamName = newTeamName;
    }

    public void updateTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }
}
