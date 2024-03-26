package com.binary.webide_be.userSearch.controller;

import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.userSearch.service.UserSearchService;
import com.binary.webide_be.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserSearchController {
    private final UserSearchService userSearchService;

    @GetMapping("/search")
    public ResponseEntity<ResponseDto<?>> searchUsersList(
            @RequestParam String searchWord,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userSearchService.searchUsersList(searchWord, userDetails));
    }
}
