package com.binary.webide_be.team.dto;

import com.binary.webide_be.user.entity.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class ModifyRequestDto {

    @NotEmpty
    private List<Long> userId; //프로젝트 생성인 ID로, 팀장이 된다
    @NotEmpty
    private Long teamId;
    @NotEmpty
    private String teamName;
    @NotEmpty
    private ArrayList<User> participants;

    @NotEmpty
    private String profileImg;
    @NotEmpty
    private String email;
    @NotEmpty
    private String nickName;

}
