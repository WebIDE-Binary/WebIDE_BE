package com.binary.webide_be.user.dto;

import lombok.Getter;

@Getter
public class UpdateUserInfoRequestDto {
    private String nickName;
    private Boolean deleteProfile;
}
