package com.binary.webide_be.team.repository;

import com.binary.webide_be.team.dto.CreateResponseDto;
import com.binary.webide_be.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface TeamRepository extends JpaRepository<Team, Long> {

    //팀 생성


    //팀 관리


}
