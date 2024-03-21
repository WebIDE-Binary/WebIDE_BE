package com.binary.webide_be.ide.repository;

import com.binary.webide_be.ide.entity.FileData;

import com.binary.webide_be.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
    List<FileData> findAllByProjectId(Project projectId);
}
