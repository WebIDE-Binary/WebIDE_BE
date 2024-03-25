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


    public ChatMessageResponseDto(ChatMessage chatMessage) {
        this.chatMessageId = chatMessage.getChatMessageId();
        this.userId = chatMessage.getSender().getUserId();
        this.userNickName = chatMessage.getSender().getNickName();
        this.userProfileImg = chatMessage.getSender().getProfileImg();
        this.chatMessage = chatMessage.getChatMessage();
        this.createdAt = chatMessage.getCreatedAt();
    }

}
