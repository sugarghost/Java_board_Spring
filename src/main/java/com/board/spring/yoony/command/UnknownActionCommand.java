package com.board.spring.yoony.command;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class UnknownActionCommand implements ActionCommand {

  @Override
  public void execute(MultipartHttpServletRequest request, Map<String, Object> paramMap,
      Map<String, Object> viewModel)
      throws ServletException, IOException {
  }
}
