package com.binary.webide_be.ide.dto;

import com.binary.webide_be.ide.entity.FileTypeEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateFileRequestDto {
    @NotNull @Min(0)
    private Long projectId;

    private Long parentId;

    @NotEmpty
    private String fileName;

    @NotNull
    private FileTypeEnum fileType;
}
