package com.binary.webide_be.team.controller;

import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.team.dto.*;
import com.binary.webide_be.team.entity.Team;
import com.binary.webide_be.team.service.TeamService;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams") //경로
@RequiredArgsConstructor
public class TeamController {
    private TeamService teamService;

    //팀 생성
    @Operation(summary = "팀 생성", description = "[팀 생성] api")
    @PostMapping("/api/teams")
    public ResponseEntity<ResponseDto<?>> createTeam (@RequestBody @Valid CreateRequestDto<?> createRequestDto,
                                                      @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails){

        return ResponseEntity.ok(teamService.createTeam(createRequestDto, userDetails)); // 유저 디테일 필요함
        //그럼 제가 이렇게 적은건 유저검증을 거치지 않고 팀생성을 하게끔 만든걸까요??

    }

    //팀 수정
    @Operation(summary = "팀 수정", description = "[팀 수정] api")

    @PatchMapping("/api/teams/{teamId}")
    public ResponseEntity<ResponseDto> modifyTeam (ModifyRequestDto modifyRequestDto,
                                                   @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(teamService.modifiedTeam(modifyRequestDto, userDetails));
    }

    // 팀 관리
//    @Operation(summary = "팀 관리 - 검색 기능", description = "[팀 관리 - 검색기능] api")
//    @GetMapping("/api/teams/find?search={email or nickname}")
//
//    public ResponseEntity<ResponseDto> manageTeam (@RequestBody ManageRequestDto manageRequestDto,
//                                                   @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        ManageRequestDto manageResponseDto = new ManageRequestDto();
//        return ResponseEntity.ok(teamService.manageTeam(manageResponseDto, userDetails));
//
//
//    }
    //팀 검색
//    public ResponseEntity<ResponseDto> searchParticipants(@RequestParam String keyword, ManageRequestDto manageRequestDto,
//                                                          @Param) {
//        List<User> participants = userService.searchParticipants(keyword);
//        return ResponseEntity.ok(participants);
//    }

}


