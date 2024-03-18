package com.binary.webide_be.chat.entity;

import com.binary.webide_be.chat.dto.ChatMessageRequestDto;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.util.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage extends TimeStamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @Column(nullable = false, length = 1000)
    private String chatMessage;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "chatRoomId")
    private ChatRoom chatRoomId;

    public static ChatMessage of(User user, ChatRoom chatRoom, ChatMessageRequestDto chatMessageRequestDto){
        return ChatMessage.builder()
                .sender(user)
                .chatRoomId(chatRoom)
                .chatMessage(chatMessageRequestDto.getChatMessage())
                .build();
    }
}
