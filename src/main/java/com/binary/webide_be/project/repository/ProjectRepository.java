package com.binary.webide_be.project.repository;

import com.binary.webide_be.project.entity.Project;
import com.binary.webide_be.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByTeamIn(List<Team> teams);
}
