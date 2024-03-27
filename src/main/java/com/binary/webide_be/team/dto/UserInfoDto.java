package com.binary.webide_be.team.dto;

import com.binary.webide_be.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private Long userId;
    private String profileImg;
    private String email;
    private String nickName;

    public UserInfoDto(User user) {
        this.userId = user.getUserId();
        this.profileImg = user.getProfileImg();
        this.email = user.getEmail();
        this.nickName = user.getNickName();
    }
}