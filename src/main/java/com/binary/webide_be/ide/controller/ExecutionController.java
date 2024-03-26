package com.binary.webide_be.ide.controller;

import com.binary.webide_be.ide.dto.ExecutionRequestDto;
import com.binary.webide_be.ide.service.JavaRunnerService;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ide/execution")
public class ExecutionController {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final JavaRunnerService javaRunnerService;

    @Operation(summary = "Java 코드 실행", description = "[Java 코드 실행] api")
    @PostMapping("/java/{fileId}")
    public ResponseEntity<ResponseDto<?>> executeJavaFile(
            @PathVariable Long fileId,
            @RequestBody @Valid ExecutionRequestDto executionRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok(javaRunnerService.executeJavaFile(bucketName, fileId, executionRequestDto, userDetails));
    }
}
