package com.binary.webide_be.team.dto;

import com.binary.webide_be.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyResponseDto<T> {

   @JsonInclude(JsonInclude.Include.NON_EMPTY)
   private int statusCode;
   @JsonInclude(JsonInclude.Include.NON_EMPTY)
   private T data;

   public static <T> ResponseEntity<ModifyResponseDto> dtoResponseEntity(HttpStatus status, T data){
      ModifyResponseDtoBuilder<T> responseDto = ModifyResponseDto.<T>builder()
              .statusCode(status.value());

       return null;
   }
}

//   public static <T> ResponseEntity<CreateResponseDto<T>> toResponseEntity(HttpStatus status, String message, T data) {
//      CreateResponseDto<T> responseDto = CreateResponseDto.<T>builder()
//              .statusCode(status.value())
//              .message(message)
//              .build();
//      return new ResponseEntity<>(responseDto, status);
//
//   }
//}