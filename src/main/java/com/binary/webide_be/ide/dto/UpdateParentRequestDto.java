package com.binary.webide_be.ide.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateParentRequestDto {
    @NotNull @Min(0)
    private Long projectId;

    Long parentId; //TODO: 얘는 왜 private가 없나요?
}
