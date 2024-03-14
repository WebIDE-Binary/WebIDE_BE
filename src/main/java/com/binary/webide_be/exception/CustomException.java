package com.binary.webide_be.exception;

import com.binary.webide_be.exception.message.ErrorMsg;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorMsg errorMsg;
}
