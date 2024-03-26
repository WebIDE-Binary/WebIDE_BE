package com.binary.webide_be.ide.controller;

import com.binary.webide_be.ide.dto.UpdateFileDataNameRequestDto;
import com.binary.webide_be.ide.service.IdeService;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ide")
public class IdeController {
    private final IdeService ideService;

    @Operation(summary = "내부페이지 - 파일 트리 조회", description = "[파일 트리 조회] api")
    @GetMapping("/{projectId}")
    public ResponseEntity<ResponseDto<?>> getFileTree(
            @PathVariable Long projectId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(ideService.getFileTree(projectId, userDetails));
    }

    @Operation(summary = "내부페이지 - 파일/폴더 삭제", description = "[파일/폴더 삭제] api")
    @DeleteMapping("/{projectId}/{fileDateId}")
    public ResponseEntity<ResponseDto<?>> deleteFileData(
            @PathVariable Long projectId,
            @PathVariable Long fileDateId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        return ResponseEntity.ok(ideService.deleteFileData(projectId, fileDateId, userDetails));
    }

    @Operation(summary = "내부페이지 - 파일/폴더 이름 변경", description = "[파일/폴더 이름 변경] api")
    @PutMapping("/{projectId}/{fileDateId}")
    public ResponseEntity<ResponseDto<?>> updateFileDataName(
            @PathVariable Long projectId,
            @PathVariable Long fileDateId,
            @RequestBody UpdateFileDataNameRequestDto updateFileDataNameRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok(ideService.updateFileDataName(projectId, fileDateId, updateFileDataNameRequestDto, userDetails));
    }



}
