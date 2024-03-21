package com.binary.webide_be.team.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateResponseDto<T> {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private  int statusCode;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T data;

//        return ResponseEntity
//                .status(getStatusCode())
//                .body(getMessage());

        public static <T> ResponseEntity<CreateResponseDto<T>> toResponseEntity(HttpStatus status, String message, T data) {
            CreateResponseDto<T> responseDto = CreateResponseDto.<T>builder()
                    .statusCode(status.value())
                    .message(message)
                    .build();
            return new ResponseEntity<>(responseDto, status);

        }
}
