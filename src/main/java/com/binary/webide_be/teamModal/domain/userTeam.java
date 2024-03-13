package com.binary.webide_be.teamModal.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class userTeam {
    @Getter @Setter
    @Id @GeneratedValue
    @Column(name = "userTeam_id")
    @JoinColumn(name = "user_id")
    //private User user;
    @JoinColumn(name = "team_id")
    private Team team;
}
