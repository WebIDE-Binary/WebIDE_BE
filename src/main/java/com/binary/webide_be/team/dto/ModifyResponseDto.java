package com.binary.webide_be.team.dto;

import com.binary.webide_be.project.entity.Team;
import com.binary.webide_be.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;

@Getter
public class ModifyResponseDto {
   private int statusCode;
   private LinkedList<User> data; //

   public LinkedList<User> getData() {
      return null;
      //....???????????/?
      //팀 아이디,이름, 참가자를 가져와야 하는데; 어쩌지
      //자동으로 생성해주나? _>

   }
}
