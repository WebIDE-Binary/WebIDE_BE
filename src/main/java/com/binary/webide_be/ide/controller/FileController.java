package com.binary.webide_be.ide.controller;

import com.binary.webide_be.ide.dto.CreateFileRequestDto;
import com.binary.webide_be.ide.service.FileService;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ide/file")
public class FileController {
    private final FileService fileService;

    @PostMapping()
    public ResponseEntity<ResponseDto<?>> creatFile(
            @RequestBody @Valid CreateFileRequestDto createFileRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(fileService.createFile(createFileRequestDto, userDetails));
    }
}
