package com.board.spring.yoony.error;

import lombok.Getter;
import lombok.Setter;

/**
 * 설명
 *
 * @author YK
 * @version 1.0
 * @fileName ErrorResponse
 * @since 2023-02-24
 */
@Getter
@Setter
public class ErrorResponse {

  private int status;
  private String message;
  private String code;

  public ErrorResponse(ErrorCode errorCode) {
    this.status = errorCode.getStatus();
    this.message = errorCode.getMessage();
    this.code = errorCode.getErrorCode();
  }
}