package com.binary.webide_be.team.repository;

import com.binary.webide_be.team.entity.Team;
import com.binary.webide_be.team.entity.UserTeam;
import com.binary.webide_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

    //사용자와 팀을 기반으로 UserTeam 엔티티를 찾는 메서드
    Optional<UserTeam> findByUserAndTeam(User user, Team team);
}
