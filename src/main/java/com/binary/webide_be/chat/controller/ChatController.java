package com.binary.webide_be.chat.controller;

import com.binary.webide_be.chat.dto.ChatMessageRequestDto;
import com.binary.webide_be.chat.dto.ChatMessageResponseDto;
import com.binary.webide_be.chat.service.ChatService;
import com.binary.webide_be.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate template; //특정 Broker 로 메세지를 전달
    private final ChatService chatService;

    @MessageMapping("/{chatRoomId}")
//    @SendTo("/room/{chatRoomId}")
    public void message(
            @DestinationVariable Long chatRoomId,
            ChatMessageRequestDto chatMessageRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ChatMessageResponseDto responseDto = chatService.createChatMessage(chatRoomId, chatMessageRequestDto, userDetails);
        template.convertAndSend("/sub/room/" + chatRoomId, responseDto);
//        return responseDto;
    }

}