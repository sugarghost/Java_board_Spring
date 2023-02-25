package com.board.spring.yoony.error;

import lombok.Getter;

@Getter
public class CustomExceptionView extends RuntimeException {

  private final ErrorCode errorCode;

  public CustomExceptionView(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
