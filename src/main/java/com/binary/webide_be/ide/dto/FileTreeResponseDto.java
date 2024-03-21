package com.binary.webide_be.ide.dto;

import com.binary.webide_be.ide.entity.FileData;
import com.binary.webide_be.ide.entity.FileTypeEnum;
import lombok.Getter;

@Getter
public class FileTreeResponseDto {
    private Long fileId;
    private Long parentId;
    private FileTypeEnum fileType;
    private String fileName;
    private String fileS3Address;

    public FileTreeResponseDto(FileData fileData) {
        this.fileId = fileData.getFileId();
        // fileData의 getParentId()가 null인 경우, this.parentId에도 null을 할당
        this.parentId = (fileData.getParentId() != null) ? fileData.getParentId().getFileId() : null;
        this.fileType = fileData.getFileType();
        this.fileName = fileData.getFileName();
        this.fileS3Address = fileData.getFileS3Address();
    }
}
