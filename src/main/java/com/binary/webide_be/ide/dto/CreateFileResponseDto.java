package com.binary.webide_be.ide.dto;

import com.binary.webide_be.project.entity.Project;
import lombok.Getter;

@Getter
public class CreateFileResponseDto {
    private Long projectId;
    private FileTreeResponseDto file;

    public CreateFileResponseDto(Project project, FileTreeResponseDto file) {
        this.projectId = project.getProjectId();
        this.file = file;
    }
}