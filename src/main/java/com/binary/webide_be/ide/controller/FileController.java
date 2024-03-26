package com.binary.webide_be.ide.controller;

import com.binary.webide_be.ide.dto.CreateFileRequestDto;
import com.binary.webide_be.ide.dto.UpdateFileContentRequestDto;
import com.binary.webide_be.ide.dto.UpdateFilePathRequestDto;
import com.binary.webide_be.ide.service.FileService;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @Operation(summary = "파일 내용 변경", description = "[파일 내용 변경] api")
    @PutMapping(value = "/{fileId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<?>> updateCodeFile(
            @PathVariable Long fileId,
            @RequestPart(value = "codeFile") MultipartFile codeFile,
            @RequestPart(value = "updateFileContentRequestDto") UpdateFileContentRequestDto updateFileContentRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        return ResponseEntity.ok(fileService.updateCodeFile(fileId, codeFile, updateFileContentRequestDto, userDetails));
    }
}
