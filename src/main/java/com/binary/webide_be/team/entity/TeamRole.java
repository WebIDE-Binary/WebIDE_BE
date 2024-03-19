package com.binary.webide_be.team.entity;

import com.binary.webide_be.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public enum TeamRole {
    LEADER(TeamRole.Authority.LEADER);

    private final String authority;
    TeamRole(String authority) {this.authority = authority;}

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String LEADER = "ROLE_LEADER";
    }
}
