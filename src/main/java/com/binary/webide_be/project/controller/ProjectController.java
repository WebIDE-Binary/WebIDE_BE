package com.binary.webide_be.project.controller;

import com.binary.webide_be.project.dto.CreateProjectRequestDto;
import com.binary.webide_be.project.dto.UpdateProjectRequestDto;
import com.binary.webide_be.project.service.ProjectService;
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
@RequestMapping("api/projects")
public class ProjectController {

    public final ProjectService projectService;

    //프로젝트 생성
    @Operation(summary = "프로젝트 생성", description = "[프로젝트 생성] api")
    @PostMapping
    public ResponseEntity<ResponseDto<?>> projectCreate(@RequestBody CreateProjectRequestDto createProjectRequestDto,
                                                        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(projectService.createProject(createProjectRequestDto, userDetails)); //응답으로 보내줄 정보

    }

    //프로젝트 수정
    @Operation(summary = "프로젝트 수정", description = "[프로젝트 수정] api")
    @PatchMapping("/{projectId}")
    public ResponseEntity<ResponseDto<?>> projectUpdate(@PathVariable Long projectId,
                                                        @RequestBody UpdateProjectRequestDto updateProjectRequestDto,
                                                        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) { //인증된 사용자의 정보가 담겨있음 유저의 ID(이미일)등등
        return ResponseEntity.ok(projectService.updateProject(projectId, updateProjectRequestDto, userDetails));
    }

    //프로젝트 삭제
    @Operation(summary = "프로젝트 삭제",description = "[프로젝트 수정] api")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ResponseDto<?>>projectDelete(@PathVariable Long projectId,
                                                        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) { //인증된 사용자의 정보가 담겨있음 유저의 ID(이미일)등등
        return ResponseEntity.ok(projectService.deleteProject(projectId, userDetails));
    }

    //내 프로젝트 목록 조회 (조회는 RequestParam이 국룰이다)
    @Operation(summary = "내 프로젝트 목록 조회", description = "[프로젝트 목록 조회] api")
    @GetMapping
    public ResponseEntity<ResponseDto<?>> projectList(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @RequestParam(required = false) String searchWord) { //요청으로 들어온 정보는 유저가 권한이 있는지 + 파라미터 정보
        return ResponseEntity.ok(projectService.findProjects(searchWord, userDetails));
    }


}
