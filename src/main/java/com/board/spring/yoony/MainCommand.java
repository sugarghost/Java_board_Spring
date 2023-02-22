package com.board.spring.yoony;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface MainCommand {

  String execute(HttpServletRequest request, Map<String, Object> model)
      throws ServletException, IOException;
}
