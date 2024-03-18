package com.binary.webide_be.chat.service;

import com.binary.webide_be.chat.dto.ChatMessageRequestDto;
import com.binary.webide_be.chat.dto.ChatMessageResponseDto;
import com.binary.webide_be.chat.entity.ChatMessage;
import com.binary.webide_be.chat.entity.ChatRoom;
import com.binary.webide_be.chat.repository.ChatMessageRepository;
import com.binary.webide_be.chat.repository.ChatRoomRepository;
import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.user.repository.UserRepository;
import com.binary.webide_be.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.binary.webide_be.exception.message.ErrorMsg.CHAT_ROOM_NOT_FOUND;
import static com.binary.webide_be.exception.message.SuccessMsg.CHAT_HISTORY_SUCCESS;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ResponseDto<?> getChatHistory(Long chatRoomId) {
        // 채팅방 ID로 ChatRoom 엔티티 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoom);

        List<ChatMessageResponseDto> chatHistory = messages.stream().map(message -> {
            return ChatMessageResponseDto.builder()
                    .chatMessageId(message.getChatMessageId())
                    .userId(message.getSender().getId())
                    .userNickName(message.getSender().getNickName())
                    .userProfileImg(message.getSender().getProfileImg())
                    .chatMessage(message.getChatMessage())
                    .createdAt(message.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());

        return ResponseDto.builder()
                .statusCode(CHAT_HISTORY_SUCCESS.getHttpStatus().value())
                .data(chatHistory)
                .build();
    }

    public ChatMessageResponseDto createChatMessage(Long chatRoomId, ChatMessageRequestDto chatMessageRequestDto) {
        // 사용자 ID로 User 엔티티 조회
        User user = userRepository.findById(chatMessageRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + chatMessageRequestDto.getUserId()));

        // 채팅방 ID로 ChatRoom 엔티티 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat room ID: " + chatRoomId));

        // ChatMessage 인스턴스 생성
        ChatMessage chatMessage = ChatMessage.of(user, chatRoom, chatMessageRequestDto);

        return ChatMessageResponseDto.from(chatMessage);
    }

}
