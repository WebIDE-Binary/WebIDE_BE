package com.binary.webide_be.user.dto;

import com.binary.webide_be.user.entity.User;

public class UpdateUserInfoResponseDto {
    private String email;
    private String nickName;
    private String profileImg;

    public UpdateUserInfoResponseDto(User user, String profileImg) {
        this.email = user.getEmail();
        this.nickName = user.getNickName();
        this.profileImg = profileImg;
    }
}
