package com.example.be.common.exception;

import com.example.be.common.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(value = GlobalException.class)
    public ResponseEntity<?> handleGlobalException(GlobalException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ResponseDto.fail(e.getErrorCode().getCode(), e.getErrorCode().getMessage()));
    }
}
