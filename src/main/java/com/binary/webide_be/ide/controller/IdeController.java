package com.binary.webide_be.ide.controller;

import com.binary.webide_be.ide.service.IdeService;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/ide")
public class IdeController {
    private final IdeService ideService;

    @GetMapping("/{projectId}")
    public ResponseEntity<ResponseDto<?>> getFileTree(
            @PathVariable Long projectId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(ideService.getFileTree(projectId, userDetails));
    }


}
