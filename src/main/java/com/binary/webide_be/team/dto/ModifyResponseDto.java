package com.binary.webide_be.team.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyResponseDto<T> {

   private Long teamId;
   private String teamName;
   private ArrayList<ParticipantsDto<T>> participants = new ArrayList<>();


   public void setData(T data) {
      this.teamName = getTeamName();
      this.teamId = getTeamId();
      this.participants = getParticipants();
   }

}