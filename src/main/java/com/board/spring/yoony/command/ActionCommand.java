package com.board.spring.yoony.command;

import com.board.spring.yoony.error.CustomException;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface ActionCommand {

  void execute(HttpServletRequest request, Map<String, Object> paramMap,
      Map<String, Object> viewModel)
      throws Exception;
}
