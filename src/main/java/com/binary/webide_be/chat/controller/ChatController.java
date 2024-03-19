package com.binary.webide_be.chat.controller;

import com.binary.webide_be.chat.dto.ChatMessageRequestDto;
import com.binary.webide_be.chat.dto.ChatMessageResponseDto;
import com.binary.webide_be.chat.entity.ChatMessage;
import com.binary.webide_be.chat.service.ChatService;
import com.binary.webide_be.user.dto.CustomEmail;
import com.binary.webide_be.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final SimpMessagingTemplate template; //특정 Broker 로 메세지를 전달
    private final ChatService chatService;

    @Operation(summary = "채팅방 - 채팅 내역 조회", description = "[채팅방] api")
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ResponseDto<?>> chatHistory(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatService.getChatHistory(chatRoomId));
    }

    // WebSocketConfig에서 설정한 applicationDestinationPrefixes 와 @MessageMapping 경로가 병합
    // /pub/{chatRoomId}/message
    // /sub/{chatRoomId}/message/topic
    @MessageMapping("/{chatRoomId}/message")
    @SendTo("/{chatRoomId}/message/topic")
    public ChatMessageResponseDto message(@DestinationVariable Long chatRoomId, ChatMessageRequestDto chatMessageRequestDto) {
        // 채팅 메시지 생성 및 DTO 변환
        ChatMessageResponseDto savedChatMessage = chatService.createChatMessage(chatRoomId, chatMessageRequestDto);

        // 변환된 DTO를 해당 채팅방 구독자들에게 전송
        return savedChatMessage;
     }

}