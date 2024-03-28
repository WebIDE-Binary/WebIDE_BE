package com.binary.webide_be.chat.controller;

import com.binary.webide_be.chat.service.ChatService;
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
@RequestMapping("/api/chat")
public class ChatRoomController {
    private final ChatService chatService;

    @Operation(summary = "채팅방 - 채팅 내역 조회", description = "[채팅방] api")
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ResponseDto<?>> messageList(
            @PathVariable Long chatRoomId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(chatService.messageList(chatRoomId, userDetails));
    }
}