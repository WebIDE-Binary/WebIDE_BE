package com.binary.webide_be.team.repository;

import com.binary.webide_be.team.entity.Team;
import com.binary.webide_be.team.entity.TeamRoleEnum;
import com.binary.webide_be.team.entity.UserTeam;
import com.binary.webide_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

    //사용자와 팀을 기반으로 UserTeam 엔티티를 찾는 메서드
    Optional<UserTeam> findByUserAndTeam(User user, Team team);

    //user엔티티 인스턴스에 해당하는 모든 UserTeam엔티티를 데이터 베이스에서 조회함 -> User엔티티가 참여하고 있는 UserTeam관계목록을 반환함 -> UserTeam 목록을 통해 해당 사용자가 속한 모든 팀의 정보를 알 수 있다.
    List<UserTeam> findByUser(User user);
    List<UserTeam> findByTeam(Team team);
}
