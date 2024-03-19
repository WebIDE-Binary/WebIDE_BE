package com.binary.webide_be.project.repository;

import com.binary.webide_be.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
