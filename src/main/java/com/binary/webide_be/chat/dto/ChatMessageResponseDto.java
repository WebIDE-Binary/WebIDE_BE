package com.binary.webide_be.chat.dto;

import com.binary.webide_be.chat.entity.ChatMessage;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.util.entity.TimeStamped;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResponseDto {
    private Long chatMessageId;
    private Long userId;
    private String userNickName;
    private String userProfileImg;
    private String chatMessage;
    private LocalDateTime createdAt;

    @Builder
    public ChatMessageResponseDto(Long chatMessageId, Long userId, String userNickName, String userProfileImg, String chatMessage, LocalDateTime createdAt) {
        this.chatMessageId = chatMessageId;
        this.userId = userId;
        this.userNickName = userNickName;
        this.userProfileImg = userProfileImg;
        this.chatMessage = chatMessage;
        this.createdAt = createdAt;
    }

    // TODO: 빌더를 없애고 생성자로 받기 => 시온님 CreatePorjectResponseDto 참고
    public static ChatMessageResponseDto from(ChatMessage chatMessage) {
        return ChatMessageResponseDto.builder()
                .chatMessageId(chatMessage.getChatMessageId())
                .userId(chatMessage.getSender().getId())
                .userNickName(chatMessage.getSender().getNickName())
                .userProfileImg(chatMessage.getSender().getProfileImg())
                .chatMessage(chatMessage.getChatMessage())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}
