package com.board.spring.yoony.article;

import com.board.spring.yoony.command.MainCommand;
import com.board.spring.yoony.article.page.PageDTO;
import com.board.spring.yoony.category.CategoryMapper;
import com.board.spring.yoony.category.CategoryDTO;
import com.board.spring.yoony.util.RequestUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleListCommand implements MainCommand {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private final SqlSessionTemplate sqlSessionTemplate;

  @Override
  public String execute(HttpServletRequest request, Map<String, Object> paramMap, Map<String, Object> viewModel) {
    logger.debug("execute()");
    String viewPage = "board/free/list";
    try {
      ArticleDAO articleDAO = sqlSessionTemplate.getMapper(ArticleDAO.class);
      CategoryMapper categoryMapper = sqlSessionTemplate.getMapper(CategoryMapper.class);

      List<CategoryDTO> categoryList = categoryMapper.selectCategoryList();
      viewModel.put("categoryList", categoryList);

      // 페이지네이션 구현
      int totalCount = articleDAO.selectArticleCount(paramMap);
      int pageSize = 5;
      int blockSize = 10;

      int pageNum = RequestUtil.getIntParameter(request, "pageNum");
      if (pageNum == 0) {
        pageNum = 1;
      }
      PageDTO pageDTO = new PageDTO(pageNum, pageSize, blockSize, totalCount);
      viewModel.put("pageDTO", pageDTO);
      paramMap.put("rowStart", pageDTO.getStartRowNum());
      paramMap.put("pageSize", pageSize);

      // 검색조건에 따른 게시글 가져옴
      List<ArticleDTO> articleList = articleDAO.selectArticleList(paramMap);
      viewModel.put("articleList", articleList);
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
    return viewPage;
  }
}
