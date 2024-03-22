package com.binary.webide_be.ide.controller;

import com.binary.webide_be.ide.dto.CreateFolderRequestDto;
import com.binary.webide_be.ide.service.FolderService;
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
@RequestMapping("/api/ide/folders")
public class FolderController {
    private final FolderService folderService;

    @PostMapping()
    public ResponseEntity<ResponseDto<?>> creatFolder(
            @RequestBody @Valid CreateFolderRequestDto createFolderRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(folderService.createFolder(createFolderRequestDto, userDetails));
    }
}
