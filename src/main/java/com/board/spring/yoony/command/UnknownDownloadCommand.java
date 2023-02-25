package com.board.spring.yoony.command;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UnknownDownloadCommand implements DownloadCommand {

  @Override
  public ResponseEntity execute(HttpServletRequest request)
      throws Exception {
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }
}
