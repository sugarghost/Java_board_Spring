package com.board.spring.yoony;

import com.board.spring.yoony.article.search.SearchManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 설명
 *
 * @author YK
 * @version 1.0
 * @fileName FrontController
 * @since 2023-02-22
 */
@Controller
@RequestMapping("/{pathCommand}.do")
public class FrontController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @RequestMapping
  public ModelAndView handleAllRequests(HttpServletRequest request, @PathVariable("pathCommand") String pathCommand)
      throws ServletException, IOException {
    // 요청 응답에 대한 인코딩을 UTF-8로 설정
    request.setCharacterEncoding("UTF-8");

    SearchManager searchManager = new SearchManager(request);
    request.setAttribute("searchManager", searchManager);


    MainCommand mainCommand = MainCommandHelper.getCommand(pathCommand);

    Map<String, Object> model = new HashMap<>();
    model.put("searchManager", searchManager);
    String viewName = mainCommand.execute(request, model);

    ModelAndView modelAndView = new ModelAndView(viewName, model);
    return modelAndView;
  }
}