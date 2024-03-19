package com.binary.webide_be.team.entity;

import com.binary.webide_be.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserTeam {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private TeamRoleEnum teamRoleEnum;

    public UserTeam(User user, Team team, TeamRoleEnum teamRoleEnum) {
        this.user = user;
        this.team = team;
        this.teamRoleEnum = teamRoleEnum;
    }
}
