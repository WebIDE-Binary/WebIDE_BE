package com.binary.webide_be.ide.dto;

import lombok.Getter;

@Getter
public class UpdateFileContentResponseDto {
    private String fileData;

    public UpdateFileContentResponseDto(String fileData) {
        this.fileData = fileData;
    }
}
