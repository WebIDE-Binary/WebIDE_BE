package com.binary.webide_be.chat.entity;

import com.binary.webide_be.project.entity.Team;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ChatRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private Team teamId;

    @OneToMany(mappedBy = "chatRoomId")
    private List<ChatMessage> chatMessagesLists = new ArrayList<>();

}
