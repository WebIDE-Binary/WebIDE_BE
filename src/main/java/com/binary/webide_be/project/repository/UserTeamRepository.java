package com.binary.webide_be.project.repository;

import com.binary.webide_be.project.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
}
