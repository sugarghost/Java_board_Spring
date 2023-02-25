package com.board.spring.yoony.command;

import com.board.spring.yoony.error.CustomException;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * ActionCommand 인터페이스
 * <p>요청 처리를 위한 인터페이스
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 26.
 */
public interface ActionCommand {

  /**
   * 요청 처리를 수행한다.
   *
   * @param request  HttpServletRequest
   * @param paramMap Map<String, Object> 처리에 필요한 파라미터를 담은 맵
   * @param model    Map<String, Object> 처리 결과를 담을 맵
   * @throws Exception
   * @throws CustomException
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  void execute(HttpServletRequest request, Map<String, Object> paramMap,
      Map<String, Object> model)
      throws Exception;
}
