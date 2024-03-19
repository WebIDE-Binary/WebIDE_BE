package com.binary.webide_be.team.dto;

import com.binary.webide_be.user.entity.User;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;

public class ModifyRequestDto {

    @NotEmpty
    private Long userID; //프로젝트 생성인 ID로, 팀장이 된다

    @NotEmpty
    private String programming_Languages;

    @NotEmpty
    private String projectName;
    private String projectExplanation; //null OK

    @NotEmpty
    private Long teamName;
    private ArrayList<User> participants;
}
