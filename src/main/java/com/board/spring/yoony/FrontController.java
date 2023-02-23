package com.board.spring.yoony;

import com.board.spring.yoony.article.search.SearchManager;
import com.board.spring.yoony.command.MainCommand;
import com.board.spring.yoony.command.MainCommandHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/{pathCommand}.do")
@RequiredArgsConstructor
public class FrontController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private final SqlSessionTemplate sqlSessionTemplate;
  @Autowired
  private MainCommandHelper mainCommandHelper;

  @RequestMapping
  public ModelAndView handleAllRequests(HttpServletRequest request,
      @PathVariable("pathCommand") String pathCommand)
      throws ServletException, IOException {
    // 요청 응답에 대한 인코딩을 UTF-8로 설정
    request.setCharacterEncoding("UTF-8");

    Map<String, Object> paramMap = new HashMap<>();
    Map<String, Object> viewModel = new HashMap<>();

    SearchManager searchManager = new SearchManager(
        request.getParameter("pageNum"),
        request.getParameter("searchWord"),
        request.getParameter("categoryId"),
        request.getParameter("startDate"),
        request.getParameter("endDate")
    );
    paramMap.putAll(searchManager.getSearchParamsMap());

    MainCommand mainCommand = mainCommandHelper.getCommand(pathCommand);
    viewModel.put("searchManager", searchManager);
    String viewName = mainCommand.execute(request, paramMap, viewModel);

    ModelAndView modelAndView = new ModelAndView(viewName, viewModel);
    return modelAndView;
  }
}