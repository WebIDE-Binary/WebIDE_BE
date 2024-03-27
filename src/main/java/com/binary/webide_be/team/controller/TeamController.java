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
    @PostMapping()
    public ResponseEntity<ResponseDto<?>> createTeam (
            @RequestBody @Valid CreateTeamRequestDto createTeamRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(teamService.createTeam(createTeamRequestDto, userDetails));
    }

    //팀 수정
    @Operation(summary = "팀 수정", description = "[팀 수정] api")
    @PatchMapping("/{teamId}")
    public ResponseEntity<ResponseDto<?>> modifyTeam(
            @PathVariable Long teamId,
            @RequestBody @Valid ModifyTeamRequestDto modifyTeamRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(teamService.updateTeamMembers(teamId, modifyTeamRequestDto, userDetails));
    }

    //팀 목록 조회
    @Operation(summary = "팀 목록 조회", description = "[팀 목록, 연관된 채팅방 조회] api")
    @GetMapping
    public ResponseEntity<ResponseDto<?>> teamList(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(teamService.findTeam(userDetails));
    }

    //팀 정보 조회
    @Operation(summary = "팀 목록 조회", description = "[팀 목록, 연관된 채팅방 조회] api")
    @GetMapping("/{teamId}")
    public ResponseEntity<ResponseDto<?>> getTeamInfo(
            @PathVariable Long teamId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(teamService.getTeamInfo(teamId, userDetails));
    }

}


