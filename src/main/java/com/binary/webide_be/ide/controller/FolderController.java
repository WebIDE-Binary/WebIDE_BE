package com.binary.webide_be.ide.controller;

import com.binary.webide_be.ide.dto.CreateFolderRequestDto;
import com.binary.webide_be.ide.dto.UpdateFolderPathRequestDto;
import com.binary.webide_be.ide.service.FolderService;
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
@RequestMapping("/api/ide/folders")
public class FolderController {
    private final FolderService folderService;

    @Operation(summary = "폴더 생성", description = "[폴더 생성] api")
    @PostMapping()
    public ResponseEntity<ResponseDto<?>> creatFolder(
            @RequestBody @Valid CreateFolderRequestDto createFolderRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(folderService.createFolder(createFolderRequestDto, userDetails));
    }

    @Operation(summary = "폴더 경로 변경", description = "[폴더 경로 변경] api")
    @PatchMapping("/{folderId}")
    public ResponseEntity<?> updateFolderPath(
            @PathVariable Long folderId,
            @RequestBody @Valid UpdateFolderPathRequestDto updateFolderPathRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(folderService.updateFolderPath(folderId, updateFolderPathRequestDto, userDetails));
    }
}
