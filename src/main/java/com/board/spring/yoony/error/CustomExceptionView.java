package com.board.spring.yoony.error;

import lombok.Getter;

@Getter
public class CustomExceptionView extends RuntimeException {

  private final ErrorCode errorCode;

  public CustomExceptionView(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }
}
