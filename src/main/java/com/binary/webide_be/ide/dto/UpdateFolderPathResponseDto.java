package com.binary.webide_be.ide.dto;

import com.binary.webide_be.project.entity.Project;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UpdateFolderPathResponseDto {
    private Long projectId;
    private List<FileTreeResponseDto> fileTree = new ArrayList<>();

    public UpdateFolderPathResponseDto(Project project, List<FileTreeResponseDto> fileTree) {
        this.projectId = project.getProjectId();
        this.fileTree = fileTree;
    }
}
