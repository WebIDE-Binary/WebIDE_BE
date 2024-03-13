package com.binary.webide_be.teamModal.controller;

import com.binary.webide_be.teamModal.dto.TeamCreateDto;
import com.binary.webide_be.teamModal.model.Team;
import com.binary.webide_be.teamModal.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams") //경로
public class TeamController {

    private TeamService teamService;

    @Autowired
    public void TeamController(TeamService teamService) {

        this.teamService = teamService;
    }
    @PostMapping //POST 메소드 정의
    public ResponseEntity<Team> creatTeam (TeamCreateDto teamCreateDto){
        //ResponseEntity<Team>은 Team객체와 함께, Status 반환
        Team team = teamService.createTeam(teamCreateDto);
        return new ResponseEntity<Team>(team, HttpStatus.CREATED);

    }


}