package com.board.spring.yoony.command;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 알 수 없는 파일 다운로드를 요청을 위한 커맨드
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 26.
 */
public class UnknownDownloadCommand implements DownloadCommand {

  /**
   * 처리를 수행한다.
   *
   * @param request HttpServletRequest
   * @return ResponseEntity
   * @throws Exception
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  @Override
  public ResponseEntity execute(HttpServletRequest request)
      throws Exception {
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }
}
