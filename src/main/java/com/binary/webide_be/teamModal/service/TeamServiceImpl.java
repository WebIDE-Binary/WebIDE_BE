package com.binary.webide_be.teamModal.service;

import com.binary.webide_be.teamModal.dto.TeamCreateDto;
import com.binary.webide_be.teamModal.model.Team;
import com.binary.webide_be.teamModal.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamServiceImpl extends TeamService {
    private TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Autowired
    public void TeamServiceImple(TeamRepository teamRepository){

        this.teamRepository = teamRepository;
    }


    @Transactional

    public Team createTeam(TeamCreateDto teamCreateDto){
        Team team = new Team();
        team.setName(teamCreateDto.getName());

        //어쩌고

        return team;
    }
}
