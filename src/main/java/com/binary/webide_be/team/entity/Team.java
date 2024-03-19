package com.binary.webide_be.team.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private int teamSize;

    Team(String teamName, int teamSize) {
        this.teamName = teamName;
        this.teamSize = teamSize;
    }
}
