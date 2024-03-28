package com.binary.webide_be.chat.service;

import com.binary.webide_be.chat.dto.ChatMessageRequestDto;
import com.binary.webide_be.chat.dto.ChatMessageResponseDto;
import com.binary.webide_be.chat.entity.ChatMessage;
import com.binary.webide_be.chat.entity.ChatRoom;
import com.binary.webide_be.chat.repository.ChatMessageRepository;
import com.binary.webide_be.chat.repository.ChatRoomRepository;
import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.user.repository.UserRepository;
import com.binary.webide_be.util.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.binary.webide_be.exception.message.ErrorMsg.CHAT_ROOM_NOT_FOUND;
import static com.binary.webide_be.exception.message.ErrorMsg.USER_NOT_FOUND;
import static com.binary.webide_be.exception.message.SuccessMsg.CHAT_HISTORY_SUCCESS;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ResponseDto<?> messageList(Long chatRoomId, UserDetailsImpl userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoom);

        List<ChatMessageResponseDto> ChatMessageHistory = messages.stream()
                .map(ChatMessageResponseDto::new)
                .collect(Collectors.toList());

        return ResponseDto.builder()
                .statusCode(CHAT_HISTORY_SUCCESS.getHttpStatus().value())
                .data(ChatMessageHistory)
                .build();
    }

    @Transactional
    public ChatMessageResponseDto createChatMessage(Long chatRoomId, ChatMessageRequestDto chatMessageRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findById(chatMessageRequestDto.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));

        ChatMessage chatMessage = new ChatMessage(user, chatRoom, chatMessageRequestDto);

        chatMessage = chatMessageRepository.save(chatMessage);

        return new ChatMessageResponseDto(chatMessage);
    }

}
