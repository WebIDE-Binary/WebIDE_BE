package com.binary.webide_be.team.controller;

import com.binary.webide_be.team.dto.*;
import com.binary.webide_be.team.entity.Team;
import com.binary.webide_be.team.service.TeamService;
import com.binary.webide_be.user.dto.SignupRequestDto;
import com.binary.webide_be.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
    @RequestMapping("/api/teams") //경로
    public class TeamController {

        private TeamService teamService;

        @Autowired
        public void TeamController(TeamService teamService) {

            this.teamService = teamService;
        }

        //팀 생성
        @Operation(summary = "팀 생성", description = "[팀 생성] api")
        @PostMapping("/api/teams")
        public ResponseEntity<Team> createTeam (@RequestBody CreateRequestDto createRequestDto){

            return ResponseEntity.ok(teamService.createTeam()); // 유저 디테일 필요함
            //그럼 제가 이렇게 적은건 유저검증을 거치지 않고 팀생성을 하게끔 만든걸까요??

        }

        //팀 수정
        @Operation(summary = "팀 수정", description = "[팀 수정] api")

        @PatchMapping("/api/teams/{teamId}")
        public  ResponseEntity<Team> modifyTeam (ModifyRequestDto modifyRequestDto, HttpServletResponse response) {

            return ResponseEntity.ok((Team) teamService.modifiedTeam(modifyRequestDto, response));
        }

        // 팀 관리
        @GetMapping("/api/teams")

        public  ResponseEntity<Team> manageTeam (@RequestBody ManageRequestDto manageRequestDto, HttpServletResponse response) {

            ManageRequestDto manageResponseDto = new ManageRequestDto();
            return ResponseEntity.ok(teamService.manageTeam(manageResponseDto, response));
        }

    }


