package com.board.spring.yoony.article;

import com.board.spring.yoony.MainCommand;
import com.board.spring.yoony.article.page.PageDTO;
import com.board.spring.yoony.category.CategoryDAO;
import com.board.spring.yoony.category.CategoryDTO;
import com.board.spring.yoony.util.RequestUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleListCommand implements MainCommand {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ArticleDAO articleDAO;
  @Autowired
  private CategoryDAO categoryDAO;

  @Override
  public String execute(HttpServletRequest request, Map<String, Object> model) {
    logger.debug("execute()");

    String viewPage = "list";
    try {
      logger.debug("testInt!");
      logger.debug("testInt: " + categoryDAO.testInt());
      logger.debug("test: " + categoryDAO.test());
      List<CategoryDTO> categoryList = categoryDAO.selectCategoryList();
      logger.debug("categoryList size: " + categoryList.size());
      model.put("categoryList", categoryList);

      // 검색을 위한 param 설정
      // 새롭게 SearchManager를 만들었지만 List부분은 기존 방식을 유지
      // SearchManager는 일단은 List 이외의 페이지나 요청에서 검색을 요청하기 위한 용도로 사용될 예정
      Map<String, Object> param = new HashMap<String, Object>();
      String searchWord = request.getParameter("searchWord");
      String categoryId = request.getParameter("categoryId");
      String startDate = request.getParameter("startDate");
      String endDate = request.getParameter("endDate");

      param.put("searchWord", searchWord);
      param.put("categoryId", categoryId);
      param.put("startDate", request.getParameter("startDate"));
      param.put("endDate", request.getParameter("endDate"));

      model.put("searchWord", searchWord);
      model.put("categoryId", categoryId);
      model.put("startDate", startDate);
      model.put("endDate", endDate);

      // 페이지네이션 구현
      int totalCount = articleDAO.selectArticleCount(param);
      logger.debug("selectArticleCount: " + totalCount);
      int pageSize = 5;
      int blockSize = 10;

      int pageNum = RequestUtil.getIntParameter(request, "pageNum");
      if (pageNum == 0) {
        pageNum = 1;
      }
      PageDTO pageDTO = new PageDTO(pageNum, pageSize, blockSize, totalCount);
      model.put("pageDTO", pageDTO);
      param.put("rowStart", pageDTO.getStartRowNum());
      param.put("pageSize", pageSize);

      // 검색조건에 따른 게시글 가져옴
      List<ArticleDTO> articleList = articleDAO.selectArticleList(param);
      logger.debug("articleList size: " + articleList.size());
      model.put("articleList", articleList);
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
    return viewPage;
  }
}
