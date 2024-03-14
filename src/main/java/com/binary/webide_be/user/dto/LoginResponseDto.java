package com.binary.webide_be.user.dto;

import com.binary.webide_be.user.entity.User;
import lombok.Getter;

@Getter
public class LoginResponseDto {
    private Long userId;
    private String email;
    private String nickName;
    private String profileImg;

    public LoginResponseDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickName = user.getNickName();
        this.profileImg = user.getProfileImg();
    }


}
