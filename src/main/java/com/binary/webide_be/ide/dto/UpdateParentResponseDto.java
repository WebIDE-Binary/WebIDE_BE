package com.binary.webide_be.ide.dto;

import com.binary.webide_be.project.entity.Project;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UpdateParentResponseDto {
    private Long projectId;
    private String projectName;
    private List<FileTreeResponseDto> fileTree = new ArrayList<>();

    public UpdateParentResponseDto(Project project, List<FileTreeResponseDto> fileTree) {
        this.projectId = project.getProjectId();
        this.projectName = project.getProjectName();
        this.fileTree = fileTree;
    }
}
