package com.board.spring.yoony.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 설명
 *
 * @author YK
 * @version 1.0
 * @fileName GlobalExceptionHandler
 * @since 2023-02-24
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    log.error("handleCustomException", ex);
    ErrorResponse response = new ErrorResponse(ex.getErrorCode());
    return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    log.error("handleException", ex);
    ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}