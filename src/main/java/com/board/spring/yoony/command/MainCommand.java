package com.board.spring.yoony.command;

import com.board.spring.yoony.error.CustomExceptionView;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * MainCommand 인터페이스
 * <p>메인(화면) 처리를 위한 인터페이스
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 26.
 */
public interface MainCommand {

  /**
   * 메인(화면) 처리를 수행한다.
   *
   * @param request   HttpServletRequest
   * @param paramMap  Map<String, Object> 처리에 필요한 파라미터를 담은 맵
   * @param viewModel Map<String, Object> 처리 결과를 담을 맵
   * @return String
   * @throws Exception
   * @throws CustomExceptionView
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  String execute(HttpServletRequest request, Map<String, Object> paramMap,
      Map<String, Object> viewModel)
      throws Exception;
}
