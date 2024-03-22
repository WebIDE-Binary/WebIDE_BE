package com.binary.webide_be.ide.dto;

import com.binary.webide_be.ide.entity.FileTypeEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateFolderRequestDto {
    // 필드 이름을 폴더라고 하는게 좋지만, 프론트에서의 처리가 원활하도록 파일이라고 하였습니다.

    @NotNull @Min(0)
    private Long projectId;

    private Long parentId;

    @NotEmpty
    private String fileName;

    @NotNull
    private FileTypeEnum fileType;
}
