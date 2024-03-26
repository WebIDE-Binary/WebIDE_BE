package com.binary.webide_be.ide.dto;

import com.binary.webide_be.ide.entity.FileData;
import lombok.Getter;

@Getter
public class UpdateFileDataNameResponseDto {
    private String newName;

    public UpdateFileDataNameResponseDto(FileData fileData) {
        this.newName = fileData.getFileName();
    }
}
