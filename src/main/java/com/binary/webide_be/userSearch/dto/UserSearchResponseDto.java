package com.binary.webide_be.userSearch.dto;

import com.binary.webide_be.user.entity.User;
import lombok.Getter;

@Getter
public class UserSearchResponseDto {
    private Long userId;
    private String nickName;
    private String profileImg;
    private String email;

    public UserSearchResponseDto(User user) {
        this.userId = user.getUserId();
        this.nickName = user.getNickName();
        this.profileImg = user.getProfileImg();
        this.email = user.getEmail();
    }
}
