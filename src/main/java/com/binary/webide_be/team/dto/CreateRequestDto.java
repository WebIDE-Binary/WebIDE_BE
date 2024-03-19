package com.binary.webide_be.team.dto;

import com.binary.webide_be.team.entity.Team;
import com.binary.webide_be.user.entity.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class CreateRequestDto {

    @NotEmpty
    private Long userId;
    @NotEmpty
    private Long teamName;
    private ArrayList<User> participants;

}
