package com.binary.webide_be.chat.repository;

import com.binary.webide_be.chat.entity.ChatMessage;
import com.binary.webide_be.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
