package com.binary.webide_be.team.dto;

import com.binary.webide_be.user.entity.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
@Setter
public class ModifyRequestDto {

    @NotEmpty
    private Long userId; //프로젝트 생성인 ID로, 팀장이 된다
    @NotEmpty
    private String teamName;
    @NotEmpty
    private Long teamId;
    private ArrayList<User> participants;

}
