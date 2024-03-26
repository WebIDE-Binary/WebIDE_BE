package com.binary.webide_be.chat.dto;

import lombok.Getter;

@Getter
public class ChatMessageRequestDto {
    private Long userId;
    private String chatMessage;
}
