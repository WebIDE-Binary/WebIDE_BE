package com.binary.webide_be.ide.dto;

import com.binary.webide_be.project.entity.Project;
import lombok.Getter;

@Getter
public class CreateFolderResponseDto {
    // 필드 이름을 폴더라고 하는게 좋지만, 프론트에서의 처리가 원활하도록 파일이라고 하였습니다.

    private Long projectId;
    private FileTreeResponseDto file;

    public CreateFolderResponseDto(Project project, FileTreeResponseDto file) {
        this.projectId = project.getProjectId();
        this.file = file;
    }
}