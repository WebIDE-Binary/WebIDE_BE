package com.binary.webide_be.util.dto;

import com.binary.webide_be.exception.message.ErrorMsg;
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
public class ResponseDto<T> {
    private int statusCode;

    @JsonInclude(JsonInclude.Include.NON_EMPTY) //@JsonInclude: 메시지가 null인 경우 출력을 안한다라는 뜻
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY) //@JsonInclude: data가 null인 경우 출력을 안한다라는 뜻
    private T data;

    public static ResponseEntity<ResponseDto> toExceptionResponseEntity(ErrorMsg errorMsg) {
        return ResponseEntity
                .status(errorMsg.getHttpStatus())
                .body(ResponseDto.builder()
                        .statusCode(errorMsg.getHttpStatus().value())
                        .data(errorMsg.getDetail())
                        .build()
                );
    }

//    public static ResponseEntity<ResponseDto> toAllExceptionResponseEntity(ErrorMsg errorMsg) {
//        return ResponseEntity
//                .status(errorMsg.getHttpStatus().value())
//                .body(ResponseDto.builder()
//                        .statusCode(errorMsg.getHttpStatus().value())
//                        .data(errorMsg.getDetail())
//                        .build()
//                );
//    }

    public static ResponseEntity<ResponseDto<?>> toAllExceptionResponseEntity(HttpStatus httpStatus, String errorMsg) {
        return ResponseEntity
                .status(httpStatus.value())
                .body(ResponseDto.builder()
                        .statusCode(httpStatus.value())
                        .message(errorMsg)
                        .build()
                );
    }
}
