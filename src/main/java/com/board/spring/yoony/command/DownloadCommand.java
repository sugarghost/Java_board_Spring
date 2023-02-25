package com.board.spring.yoony.command;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface DownloadCommand {

  ResponseEntity execute(HttpServletRequest request)
      throws Exception;
}
