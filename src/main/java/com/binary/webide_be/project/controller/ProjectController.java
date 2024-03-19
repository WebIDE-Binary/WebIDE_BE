package com.binary.webide_be.project.controller;

import com.binary.webide_be.project.dto.CreateProjectRequestDto;
import com.binary.webide_be.project.service.ProjectService;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    public final ProjectService projectService;

    //프로젝트 생성
    @PostMapping("/api/projects")
    public ResponseEntity<ResponseDto<?>> createProject(@RequestBody CreateProjectRequestDto createProjectRequestDto,
                                                        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(projectService.createProject(createProjectRequestDto, userDetails));

    }

    //프로젝트 수정


    //프로젝트 삭제


}
