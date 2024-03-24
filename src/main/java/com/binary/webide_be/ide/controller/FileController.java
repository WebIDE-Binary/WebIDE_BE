package com.binary.webide_be.ide.controller;

import com.binary.webide_be.ide.dto.CreateFileRequestDto;
import com.binary.webide_be.ide.dto.UpdateFilePathRequestDto;
import com.binary.webide_be.ide.service.FileService;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ide/file")
public class FileController {
    private final FileService fileService;

    @Operation(summary = "파일 생성", description = "[파일 생성] api")
    @PostMapping()
    public ResponseEntity<ResponseDto<?>> creatFile(
            @RequestBody @Valid CreateFileRequestDto createFileRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok(fileService.createFile(createFileRequestDto, userDetails));
    }

    @Operation(summary = "파일 경로 변경", description = "[파일 경로 변경] api")
    @PatchMapping("/{fileId}")
    public ResponseEntity<ResponseDto<?>> updateFilePath(
            @PathVariable Long fileId,
            @RequestBody @Valid UpdateFilePathRequestDto updateFilePathRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok(fileService.updateFilePath(fileId, updateFilePathRequestDto, userDetails));
    }
}
