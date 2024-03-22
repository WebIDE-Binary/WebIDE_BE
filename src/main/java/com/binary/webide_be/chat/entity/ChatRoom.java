package com.binary.webide_be.chat.entity;

import com.binary.webide_be.team.entity.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team teamId;

    @OneToMany(mappedBy = "chatRoomId")
    private List<ChatMessage> chatMessagesLists = new ArrayList<>();

    public ChatRoom(Team teamId){
        this.teamId = teamId;
    }


}
