package com.binary.webide_be.project.repository;

import com.binary.webide_be.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}