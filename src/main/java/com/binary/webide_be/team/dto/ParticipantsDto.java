package com.binary.webide_be.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantsDto<T> {
    private Long user;
    private String profileImg;
    private String email;
    private String nickName;

    public void setParticipants(T data) {
        this.user = getUser();
        this.profileImg = getProfileImg();
        this.email = getEmail();
        this.nickName = getNickName();
    }
}
