package com.board.spring.yoony;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * 커맨드 액션에 알 수 없는 종류의 액션이 들어오면 실행되는 클래스
 *
 * @author yoony
 * @version 1.0
 * @see MainCommand
 * @see MainCommandHelper
 * @since 2023. 02. 14.
 */
public class UnknownMainCommand implements MainCommand {

  @Override
  public String execute(HttpServletRequest request, Map<String, Object> model)
      throws ServletException, IOException {
    return "error/error";
  }
}
