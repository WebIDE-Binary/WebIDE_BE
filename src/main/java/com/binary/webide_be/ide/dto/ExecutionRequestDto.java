package com.binary.webide_be.ide.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;


@Getter
public class ExecutionRequestDto {
    @NotNull @Min(0)
    private Long projectId;

    private String fileS3Address;
}
