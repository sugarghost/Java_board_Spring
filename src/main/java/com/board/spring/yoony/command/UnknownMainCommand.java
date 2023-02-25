package com.board.spring.yoony.command;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * 알 수 없는 페이지 요청을 처리하기 위한 커맨드
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 26.
 */
public class UnknownMainCommand implements MainCommand {

  /**
   * 처리를 수행한다.
   * <p>에러 페이지를 반환한다.
   *
   * @param request   HttpServletRequest
   * @param paramMap  Map<String, Object> 처리에 필요한 파라미터를 담은 맵
   * @param viewModel Map<String, Object> 처리 결과를 담을 맵
   * @return String 에러 페이지
   * @throws Exception
   */
  @Override
  public String execute(HttpServletRequest request, Map<String, Object> paramMap,
      Map<String, Object> viewModel)
      throws Exception {
    return "error/error";
  }
}
