package com.binary.webide_be.team.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Team {

    @Id @GeneratedValue

    @Column(name = "team_id", nullable = false)
    private Long id;
    @Column(name = "team_name", nullable = false)
    private String name;

    @Column(name = "team_participant", nullable = false)
    private int participant;

    @Column(name = "team_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamRole teamRole;

    Team(String name, int participant, TeamRole teamRole) {
        this.name = name;
        this.participant = participant;
        this.teamRole = TeamRole.LEADER;

    }
}
