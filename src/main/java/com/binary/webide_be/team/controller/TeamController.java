package com.binary.webide_be.team.controller;

import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.team.dto.*;
import com.binary.webide_be.team.service.TeamService;
import com.binary.webide_be.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.el.stream.Stream;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams") //경로
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

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
    public ResponseDto<?> modifyTeam(
            @PathVariable Long teamId,
            @RequestBody ModifyRequestDto modifyRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return teamService.updateTeamMembers(teamId, modifyRequestDto.getUserId(), userDetails);
    }

//    // 팀 관리
//    @Operation(summary = "팀 관리 - 검색 기능", description = "[팀 관리 - 검색기능] api")
//    @GetMapping("/api/teams/find?search={email or nickname}")
//    public ResponseEntity<ResponseDto> manageTeam (@RequestBody ManageRequestDto manageRequestDto,
//                                                   @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        ManageRequestDto manageResponseDto = new ManageRequestDto();
//        return ResponseEntity.ok(teamService.manageTeam(manageResponseDto, userDetails));
//    }
//
//    //팀 검색
//    public ResponseEntity<ResponseDto> searchParticipants(@RequestParam String keyword, ManageRequestDto manageRequestDto) {
//        List<User> participants = userService.searchParticipants(keyword);
//        return ResponseEntity.ok(participants);
//    }

    //팀 목록 조회
    @Operation(summary = "팀 목록 조회", description = "[팀 목록, 연관된 채팅방 조회] api")
    @GetMapping
    public ResponseEntity<ResponseDto<?>> teamList(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(teamService.findTeam(userDetails));
    }

}


