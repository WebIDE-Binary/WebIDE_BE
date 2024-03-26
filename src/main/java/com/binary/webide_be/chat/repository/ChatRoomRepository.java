package com.binary.webide_be.chat.repository;

import com.binary.webide_be.chat.entity.ChatMessage;
import com.binary.webide_be.chat.entity.ChatRoom;
import com.binary.webide_be.team.entity.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByTeamId (Team team);
}
