package com.binary.webide_be.ide.controller;

import com.binary.webide_be.ide.dto.CreateFolderRequestDto;
import com.binary.webide_be.ide.dto.UpdateParentRequestDto;
import com.binary.webide_be.ide.service.FolderService;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.util.dto.ResponseDto;
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

    @PostMapping()
    public ResponseEntity<ResponseDto<?>> creatFolder(
            @RequestBody @Valid CreateFolderRequestDto createFolderRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(folderService.createFolder(createFolderRequestDto, userDetails));
    }

    @PatchMapping("/{fileId}") //TODO: folderId로 변경해주셔야 합니다.
    public ResponseEntity<?> updateFileParent(
            @PathVariable Long fileId,
            @RequestBody UpdateParentRequestDto updateParentRequestDto, //TODO: UpdateParentRequestDto 적용되어있는 Validatdion가 적용되려면 @Valid 를 붙여주셔야 합니다.
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(folderService.updateFileParent(fileId, updateParentRequestDto, userDetails));
    }
}
