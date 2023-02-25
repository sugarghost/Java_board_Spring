package com.board.spring.yoony.command;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

/**
 * DownloadCommand 인터페이스
 * <p>파일 다운로드를 위한 인터페이스
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 26.
 */
public interface DownloadCommand {

  /**
   * 파일 다운로드를 수행한다.
   *
   * @param request HttpServletRequest
   * @return ResponseEntity
   * @throws Exception
   * @author yoony
   * @since 2023. 02. 26.
   */
  ResponseEntity execute(HttpServletRequest request)
      throws Exception;
}
